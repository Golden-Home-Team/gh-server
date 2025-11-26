package kr.co.goldenhome.repository

import kr.co.goldenhome.entity.Terms
import kr.co.goldenhome.entity.TermsType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TermsRepositorySpec extends Specification {

    @Autowired
    TermsRepository termsRepository

    def "termsType 이 일치하고 isActive 가 true 인 약관만 조회된다"() {
        given: "테스트 데이터 준비 (활성/비활성/다른타입)"
        // 1. 찾으려는 대상 (Type 일치, Active True)
        def targetTerms = Terms.builder().termsType(TermsType.SERVICE_USAGE_TERMS).isActive(true).title("서비스 이용약관").build()
        termsRepository.save(targetTerms)

        // 2. 제외 대상 (Type 일치, Active False)
        termsRepository.save(Terms.builder().termsType(TermsType.SERVICE_USAGE_TERMS).isActive(false).title("서비스 이용약관").build())

        // 3. 제외 대상 (Type 불일치, Active True)
        termsRepository.save(Terms.builder().termsType(TermsType.LOCATION_SERVICE_TERMS).isActive(true).title("위치 서비스 이용약관").build())

        when: "메서드 실행"
        def result = termsRepository.findByTermsTypeAndIsActiveTrue(TermsType.SERVICE_USAGE_TERMS)

        then: "검증: 비활성화된 것은 무시하고 활성화된 것 1개만 조회되어야 함"
        result.isPresent()
        result.get().termsType == TermsType.SERVICE_USAGE_TERMS
        result.get().isActive == true
        result.get().title == "서비스 이용약관"
    }

    def "일치하는 활성 약관이 없으면 Empty를 반환한다"() {
        given: "비활성화된 약관만 존재"
        termsRepository.save(Terms.builder().termsType(TermsType.SERVICE_USAGE_TERMS).isActive(false).title("서비스 이용약관").build())

        when: "조회"
        def result = termsRepository.findByTermsTypeAndIsActiveTrue(TermsType.SERVICE_USAGE_TERMS)

        then: "결과 없음"
        result.isEmpty()
    }

}
