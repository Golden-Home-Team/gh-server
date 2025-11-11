package kr.co.goldenhome.service

import kr.co.goldenhome.dto.NoticeRequest
import kr.co.goldenhome.entity.Notice
import kr.co.goldenhome.repository.NoticeRepository
import spock.lang.Specification

class NoticeServiceSpec extends Specification {

    NoticeService noticeService
    NoticeRepository noticeRepository = Mock()

    def setup() {
        noticeService = new NoticeService(noticeRepository)
    }

    def "write - noticeRepository 를 호출한다"() {
        given:
        def givenNoticeRequest = new NoticeRequest("title", "content")
        def givenUserId = 1L

        when:
        noticeService.write(givenNoticeRequest, givenUserId)

        then:
        1 * noticeRepository.save(_) >> {
            Notice notice ->
                notice.title == givenNoticeRequest.title()
                notice.content == givenNoticeRequest.content()
                notice.writerId == givenUserId
                notice
        }
    }

    def "read - noticeRepository 를 호출한다"() {
        given:
        def givenNoticeId = 1L

        when:
        noticeService.read(givenNoticeId)

        then:
        1 * noticeRepository.findById(givenNoticeId) >> Optional.of(Notice.builder().build())
    }

    def "readAll - noticeRepository 를 호출한다(lastId == null)"() {
        given:
        def givenLastId = null
        def givenPageSize = 20l

        when:
        noticeService.readAll(givenLastId, givenPageSize)

        then:
        1 * noticeRepository.findAllInfiniteScroll(givenPageSize) >> List.of(Notice.builder().build())

    }

    def "readAll - noticeRepository 를 호출한다(lastId != null)"() {
        given:
        def givenLastId = 10l
        def givenPageSize = 20l

        when:
        noticeService.readAll(givenLastId, givenPageSize)

        then:
        1 * noticeRepository.findAllInfiniteScroll(givenLastId,givenPageSize) >> List.of(Notice.builder().build())

    }

}
