package kr.co.goldenhome.service

import kr.co.goldenhome.dto.DailyDietImageResponse
import kr.co.goldenhome.dto.DailyShotImageInfoRequest
import kr.co.goldenhome.dto.DailyShotImageResponse
import kr.co.goldenhome.dto.DailyShotRequest
import kr.co.goldenhome.dto.DailyShotUpdateRequest
import kr.co.goldenhome.entity.DailyShot
import kr.co.goldenhome.implement.DailyShotAppender
import kr.co.goldenhome.implement.DailyShotImageAppender
import kr.co.goldenhome.implement.DailyShotImageReader
import kr.co.goldenhome.implement.DailyShotImageUpdater
import kr.co.goldenhome.implement.DailyShotReader
import kr.co.goldenhome.implement.DailyShotUpdater
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

class DailyShotServiceSpec extends Specification{

    DailyShotService dailyShotService
    DailyShotAppender dailyShotAppender = Mock()
    DailyShotUpdater dailyShotUpdater = Mock()
    DailyShotReader dailyShotReader = Mock()
    DailyShotImageAppender dailyShotImageAppender = Mock()
    DailyShotImageUpdater dailyShotImageUpdater = Mock()
    DailyShotImageReader dailyShotImageReader = Mock()

    def setup() {
        dailyShotService = new DailyShotService(
                dailyShotAppender,
                dailyShotUpdater,
                dailyShotReader,
                dailyShotImageAppender,
                dailyShotImageUpdater,
                dailyShotImageReader
        )
    }

    def "write - dailyShotAppender, dailyShotImageAppender 를 호출한다"() {
        given:
        def givenDailyShotImageInfoRequest = List.of(new DailyShotImageInfoRequest("INDIVIDUAL", "abc-123.jpg"))
        def givenRequest = new DailyShotRequest("건강상태양호", LocalDate.of(2025,10,10), givenDailyShotImageInfoRequest)
        def givenFacilityId = 1L
        def givenDailyShotId = 2L

        when:
        dailyShotService.write(givenRequest, givenFacilityId)

        then:
        1 * dailyShotAppender.save(*_) >> {
            Long facilityId, String content, LocalDate recordDate ->
                facilityId == givenFacilityId
                content == givenRequest.content()
                recordDate == givenRequest.recordDate()
                givenDailyShotId
        }
        1 * dailyShotImageAppender.saveAll(*_) >> {
            Long dailyShotId, List<DailyShotImageInfoRequest> dailyShotImageInfoRequest ->
                dailyShotId == givenDailyShotId
                dailyShotImageInfoRequest == givenRequest.dailyShotImageInfoRequests()
        }

    }

    def "update - dailyShotUpdater, dailyShotImageUpdater 를 호출한다"() {
        given:
        def givenDailyShotImageInfoRequest = List.of(new DailyShotImageInfoRequest("INDIVIDUAL", "abc-123.jpg"))
        def givenRequest = new DailyShotUpdateRequest("건강상태양호", givenDailyShotImageInfoRequest)
        def givenDailyShotId = 2L

        when:
        dailyShotService.update(givenRequest, givenDailyShotId)

        then:
        1 * dailyShotUpdater.update(*_) >> {
            Long dailyShotId, String content ->
                dailyShotId == givenDailyShotId
                content == givenRequest.content()
        }
        1 * dailyShotImageUpdater.update(*_) >> {
            List<DailyShotImageInfoRequest> dailyShotImageInfoRequest, Long dailyShotId ->
                dailyShotId == givenDailyShotId
                dailyShotImageInfoRequest == givenRequest.dailyShotImageInfoRequests()
        }

    }

    def "readOnMain - dailyShotReader, dailyShotImageReader 를 호출한다"() {
        given:
        def givenFacilityId = 1L
        def givenDailyShotImageResponse = new DailyShotImageResponse(1L,  "https://", LocalDateTime.of(2025,10,10,10,10))

        when:
        dailyShotService.readOnMain(givenFacilityId)

        then:
        1 * dailyShotReader.getLatestByFacilityId(*_) >> {
            Long facilityId ->
                facilityId == givenFacilityId
                Mock(DailyShot)
        }
        1 * dailyShotImageReader.getLatestByDailyShotId(*_) >> givenDailyShotImageResponse
    }

    def "readByDayOfWeek - dailyShotReader, dailyShotImageReader 를 호출한다"() {
        given:
        def givenFacilityId = 1L
        def givenDayOfWeek = DayOfWeek.MONDAY
        def givenDailyShotImageResponses = List.of(new DailyShotImageResponse(1L,  "https://", LocalDateTime.of(2025,10,10,10,10)))

        when:
        dailyShotService.readByDayOfWeek(givenFacilityId, givenDayOfWeek)

        then:
        1 * dailyShotReader.getLatestByFacilityIdAndDayOfWeek(*_) >> {
            Long facilityId, int dayOfWeek ->
                facilityId == givenFacilityId
                dayOfWeek == givenDayOfWeek.getValue()
                Mock(DailyShot)
        }
        1 * dailyShotImageReader.getImagesByDailyShotId(*_) >> givenDailyShotImageResponses
    }

}
