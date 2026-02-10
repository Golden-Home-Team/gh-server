package kr.co.goldenhome.service

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import kr.co.goldenhome.ReviewMetaData
import kr.co.goldenhome.dto.FacilitySearchResponse
import kr.co.goldenhome.entity.FacilityDocument
import kr.co.goldenhome.implement.FacilityMetaDataManager
import kr.co.goldenhome.implement.FacilityReader
import kr.co.goldenhome.implement.FacilitySearcher
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.elasticsearch.core.geo.GeoPoint
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
class FacilityQueryServiceItSpec extends Specification {

    @Autowired
    FacilityQueryService facilityQueryService

    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry

    @SpringBean
    FacilitySearcher facilitySearcher = Mock()

    @SpringBean
    FacilityReader facilityReader = Mock()

    @SpringBean
    FacilityMetaDataManager facilityMetaDataManager = Mock()

    def "정상 상황에서는 Circuit 의 상태가 CLOSED 이고 OpenSearch 쪽으로 호출이 들어간다"() {
        given:
        def givenName = "행복요양원"
        def givenAddress = null
        def expectedResponse = List.of(FacilityDocument.builder()
        .id("1")
        .institutionSymbol("123")
        .facilityType("요양원")
        .name("행복요양원")
                .address("종로구")
                .establishmentYear(2022)
                .grade("A")
                .capacity(20)
                .currentTotal(14)
                .location(new GeoPoint(12.3,12.3))
                .build()
        )

        when:
        facilityQueryService.search(givenName, givenAddress, null, null, null, 0,1,20,null,null,null,null)

        then:
        1 * facilitySearcher.search(*_) >> expectedResponse

        and:
        def circuitBreaker = circuitBreakerRegistry.getAllCircuitBreakers().stream().findFirst().get()
        circuitBreaker.state == CircuitBreaker.State.CLOSED

    }

    def "비정상 상황에서는 Circuit 의 상태가 Open 이고 RDB 쪽으로 호출이 들어간다"() {
        given:
        def givenName = "행복요양원"
        def givenAddress = null
        def config = CircuitBreakerConfig.custom()
                .slidingWindowSize(1)
                .minimumNumberOfCalls(1)
                .failureRateThreshold(50)
                .build()
        circuitBreakerRegistry.circuitBreaker("openSearch",config)
        def expectedResponse = List.of(new FacilitySearchResponse(
                1,"123","요양원","행복요양원","종로구",2023,"A",12,12,"",12.3,12.3,false,4.5
        ))

        and:
        1 * facilitySearcher.search(*_) >> {
            throw new RuntimeException()
        }
        1 * facilityMetaDataManager.getProfileUrl(*_) >> ""
        1 * facilityMetaDataManager.getReviewMetaData(*_) >> new ReviewMetaData(BigDecimal.ONE, 10, 1,2,3,4,5)

        when:
        facilityQueryService.search(givenName, givenAddress, null, null, "like", 0,1,20,null,null,null,null)

        then:
        1 * facilityReader.search(*_) >> expectedResponse

        and:
        def circuitBreaker = circuitBreakerRegistry.getAllCircuitBreakers().stream().findFirst().get()
        circuitBreaker.state == CircuitBreaker.State.OPEN

    }
}
