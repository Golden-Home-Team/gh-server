package kr.co.goldenhome.service

import kr.co.goldenhome.FacilityEventManger
import kr.co.goldenhome.entity.FacilityLike
import kr.co.goldenhome.repository.FacilityLikeCountRepository
import kr.co.goldenhome.repository.FacilityLikeRepository
import spock.lang.Specification

class FacilityLikeServiceSpec extends Specification {

    FacilityLikeService facilityLikeService
    FacilityLikeRepository facilityLikeRepository = Mock()
    FacilityLikeCountRepository facilityLikeCountRepository = Mock()
//    FacilityEventManger facilityEventManger = Mock()

    def setup() {
        facilityLikeService = new FacilityLikeService(facilityLikeRepository, facilityLikeCountRepository)
    }

    def "like - 좋아요 개수가 기존에 존재한 상태로 요청을 보내면 increase 메서드만 호출한다"() {

        when:
        facilityLikeService.like(1L, 1L)

        then:
        1 * facilityLikeRepository.save(*_)

        and:
        1 * facilityLikeCountRepository.increase(*_) >> 1
        0 * facilityLikeCountRepository.save(*_)
//        1 * facilityEventManger.saveLog(_)
    }

    def "like - 좋아요 개수가 기존에 존재하지 않는다면 save 메서드를 호출한다"() {

        when:
        facilityLikeService.like(1L, 1L)

        then:
        1 * facilityLikeRepository.save(*_)

        and:
        1 * facilityLikeCountRepository.increase(*_) >> 0
        1 * facilityLikeCountRepository.save(*_)
//        1 * facilityEventManger.saveLog(_)

    }

    def "unlike - 기존 좋아요가 존재하면서 좋아요를 삭제하는데 성공하면 좋아요 카운트를 감소한다."() {

        when:
        facilityLikeService.unlike(1L, 1L)

        then:
        1 * facilityLikeRepository.findByFacilityIdAndUserId(1L, 1L) >> Optional.of(FacilityLike.builder().facilityId(1L).userId(1L).build())

        and:
        1 * facilityLikeRepository.deleteByFacilityIdAndUserId(1L, 1L) >> 1
        1 * facilityLikeCountRepository.decrease(1L)
//        1 * facilityEventManger.saveLog(_)

    }

    def "unlike - 기존 좋아요가 존재하면서 좋아요를 삭제하는데 실패하면 좋아요 카운트를 감소시키지 않는다"() {

        when:
        facilityLikeService.unlike(1L, 1L)

        then:
        1 * facilityLikeRepository.findByFacilityIdAndUserId(1L, 1L) >> Optional.of(FacilityLike.builder().facilityId(1L).userId(1L).build())

        and:
        1 * facilityLikeRepository.deleteByFacilityIdAndUserId(1L, 1L) >> 0
        0 * facilityLikeCountRepository.decrease(1L)
//        1 * facilityEventManger.saveLog(_)

    }
}
