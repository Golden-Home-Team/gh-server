package kr.co.goldenhome.profile.service

import kr.co.goldenhome.ProfileImageApi
import kr.co.goldenhome.ProfileImageApiResponse
import kr.co.goldenhome.entity.User
import kr.co.goldenhome.infrastructure.UserRepository
import kr.co.goldenhome.profile.dto.ProfileImageRequest
import kr.co.goldenhome.profile.dto.ProfileResponse
import spock.lang.Specification

class ProfileServiceSpec extends Specification {

    ProfileService profileService
    UserRepository userRepository = Mock()
    ProfileImageApi profileImageApi = Mock()

    def setup() {
        profileService = new ProfileService(userRepository, profileImageApi)
    }

    def "get - userRepository, profileImageApi 를 호출한다"() {
        given:
        def givenUserId = 1L
        def expectedProfileImageResponse = new ProfileImageApiResponse(1L, "123-ede-image1.jpg", "https://...")

        when:
        profileService.get(givenUserId)

        then:
        1 * userRepository.findById(*_) >> {
            Long userId ->
                userId == givenUserId
                Optional.of(User.builder().build())
        }

        and:
        1 * profileImageApi.getByUserId(*_) >> {
            Long userId ->
                userId == givenUserId
                expectedProfileImageResponse
        }

    }

    def "createProfileImage - profileImageApi 를 호출한다"() {
        given:
        def givenRequest = new ProfileImageRequest("123-adb-image1.jpg")
        def givenUserId = 1L

        when:
        profileService.createProfileImage(givenRequest, givenUserId)

        then:
        1 * profileImageApi.save(*_) >> {
            String formattedImageName, Long userId ->
                formattedImageName == givenRequest.formattedImageName()
                userId == givenUserId
        }
    }


}
