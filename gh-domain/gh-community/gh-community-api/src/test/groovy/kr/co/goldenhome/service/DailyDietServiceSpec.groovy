package kr.co.goldenhome.service

import kr.co.goldenhome.dto.DailyDietImageInfoRequest
import kr.co.goldenhome.dto.DailyDietImageResponse
import kr.co.goldenhome.dto.DailyDietRequest
import kr.co.goldenhome.dto.DailyDietUpdateRequest
import kr.co.goldenhome.entity.DailyDiet
import kr.co.goldenhome.implement.DailyDietAppender
import kr.co.goldenhome.implement.DailyDietImageAppender
import kr.co.goldenhome.implement.DailyDietImageReader
import kr.co.goldenhome.implement.DailyDietImageUpdater
import kr.co.goldenhome.implement.DailyDietReader
import kr.co.goldenhome.implement.DailyDietUpdater
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class DailyDietServiceSpec extends Specification{

    DailyDietService dailyDietService
    DailyDietAppender dailyDietAppender = Mock()
    DailyDietUpdater dailyDietUpdater = Mock()
    DailyDietReader dailyDietReader = Mock()
    DailyDietImageAppender dailyDietImageAppender = Mock()
    DailyDietImageUpdater dailyDietImageUpdater = Mock()
    DailyDietImageReader dailyDietImageReader = Mock()

    def setup() {
        dailyDietService = new DailyDietService(
                dailyDietAppender,
                dailyDietUpdater,
                dailyDietReader,
                dailyDietImageAppender,
                dailyDietImageUpdater,
                dailyDietImageReader,
        )
    }

    def "write - dailyDietAppender, dailyDietImageAppender 를 호출한다"() {
        given:
        def givenImageRequest = List.of(new DailyDietImageInfoRequest("MORNING", "abc-1234"))
        def givenRequest = new DailyDietRequest("영양가득 식단", LocalDate.of(2025, 10, 10), givenImageRequest)
        def givenFacilityId = 1L
        def givenDailyDietId = 2L
        when:
        dailyDietService.write(givenRequest, givenFacilityId)

        then:
        1 * dailyDietAppender.save(*_) >> {
            Long facilityId, String content, LocalDate recordDate ->
                facilityId == 1L
                content == givenRequest.content()
                recordDate == givenRequest.recordDate()
                givenDailyDietId
        }
        1 * dailyDietImageAppender.saveAll(*_) >>{
            Long dailyDietId, List<DailyDietImageInfoRequest> dailyDietImageInfoRequests ->
                dailyDietId == givenDailyDietId
        }
    }

    def "update - dailyDietUpdater, dailyDietImageUpdater 를 호출한다"() {
        given:
        def givenImageRequest = List.of(new DailyDietImageInfoRequest("MORNING", "abc-1234"))
        def givenRequest = new DailyDietUpdateRequest("영양가득 식단", givenImageRequest)
        def givenDailyDietId = 1L

        when:
        dailyDietService.update(givenRequest, givenDailyDietId)

        then:
        1 * dailyDietUpdater.update(*_) >> {
            Long dailyDietId, String content ->
                dailyDietId == givenDailyDietId
                content == givenRequest.content()
        }
        1 * dailyDietImageUpdater.update(*_) >> {
            List<DailyDietImageInfoRequest> dailyDietImageInfoRequests, Long dailyDietId ->
                dailyDietImageInfoRequests == givenImageRequest
                dailyDietId == givenDailyDietId
        }
    }

    def "readOnMain - dailyDietUpdater, dailyDietImageUpdater 를 호출한다"() {
        given:
        def givenFacilityId = 1L
        def givenDailyDietId = 2L
        def givenImageResponse = new DailyDietImageResponse(1L, "MORNING", "https://", LocalDateTime.of(2025, 10, 10, 10, 10, 10))

        when:
        dailyDietService.readOnMain(givenFacilityId)

        then:
        1 * dailyDietReader.getLatest(*_) >> {
            Long facilityId ->
                facilityId == givenFacilityId
                givenDailyDietId
        }
        1 * dailyDietImageReader.getLatest(*_) >> {
            Long dailyDietId ->
                dailyDietId == givenDailyDietId
                givenImageResponse
        }

    }

    def "read - dailyDietReader, dailyDietImageReader 를 호출한다"() {
        given:
        def givenDailyDietId = 1L
        def givenDailyDiet = DailyDiet.builder().content("내용").recordDate(LocalDate.of(2025,10,10)).createdAt(LocalDateTime.of(2025, 10, 10, 10, 10, 10)).updatedAt(LocalDateTime.of(2025, 10, 10, 10, 10, 10)).build()
        def givenImageResponse = new DailyDietImageResponse(1L, "MORNING", "https://", LocalDateTime.of(2025, 10, 10, 10, 10, 10))


        when:
        dailyDietService.read(givenDailyDietId)

        then:
        1 * dailyDietReader.get(*_) >> {
            Long dailyDietId ->
                dailyDietId == givenDailyDietId
                givenDailyDiet
        }
        1 * dailyDietImageReader.get(*_) >> {
            Long dailyDietId ->
                dailyDietId == givenDailyDietId
                List.of(givenImageResponse)
        }

    }
}
