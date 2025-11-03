package kr.co.goldenhome.profile.service

import kr.co.goldenhome.ProfileImageApi
import kr.co.goldenhome.ProfileImageApiResponse
import kr.co.goldenhome.entity.User
import kr.co.goldenhome.exception.CustomException
import kr.co.goldenhome.exception.ErrorCode
import kr.co.goldenhome.infrastructure.PasswordProcessor
import kr.co.goldenhome.infrastructure.UserRepository
import kr.co.goldenhome.profile.dto.ProfileEmailRequest
import kr.co.goldenhome.profile.dto.ProfileImageRequest
import kr.co.goldenhome.profile.dto.ProfileLoginIdRequest
import kr.co.goldenhome.profile.dto.ProfileNameRequest
import kr.co.goldenhome.profile.dto.ProfilePasswordRequest
import kr.co.goldenhome.profile.dto.ProfilePhoneNumberRequest
import kr.co.goldenhome.profile.dto.ProfileResponse
import kr.co.goldenhome.signup.implement.SignupManager
import spock.lang.Specification

class ProfileServiceSpec extends Specification {

    ProfileService profileService
    UserRepository userRepository = Mock()
    ProfileImageApi profileImageApi = Mock()
    SignupManager signupManager = Mock()
    PasswordProcessor passwordProcessor = Mock()

    def setup() {
        profileService = new ProfileService(userRepository, profileImageApi, signupManager, passwordProcessor)
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

    def "get - userId 에 해당하는 사용자가 없으면 예외를 던진다"() {
        given:
        def givenUserId = 1L

        when:
        profileService.get(givenUserId)

        then:
        1 * userRepository.findById(*_) >> Optional.empty()

        def e = thrown(CustomException)
        e.errorCode == ErrorCode.NOT_FOUND
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

    def "modifyName - userRepository 를 호출한다"() {
        given:
        def givenRequest = new ProfileNameRequest("test2user")

        when:
        profileService.modifyName(givenRequest, 1L)

        then:
        1 * userRepository.findById(*_) >> {
            Long userId ->
                userId == 1L
                Optional.of(User.builder().build())
        }
    }

    def "modifyName - userId 에 해당하는 사용자가 없으면 예외를 던진다"() {
        given:
        def givenRequest = new ProfileNameRequest("test2user")

        when:
        profileService.modifyName(givenRequest, 1L)

        then:
        1 * userRepository.findById(*_) >> Optional.empty()

        def e = thrown(CustomException)
        e.errorCode == ErrorCode.NOT_FOUND
    }

    def "modifyLoginId - userRepository, signupManager 를 호출한다"() {
        given:
        def givenRequest = new ProfileLoginIdRequest("test2user")

        when:
        profileService.modifyLoginId(givenRequest, 1L)

        then:
        1 * userRepository.findById(*_) >> {
            Long userId ->
                userId == 1L
                Optional.of(User.builder().build())
        }
        and:
        1 * signupManager.isLoginIdDuplicated(*_)
    }

    def "modifyLoginId - userId 에 해당하는 사용자가 없으면 예외를 던진다"() {
        given:
        def givenRequest = new ProfileLoginIdRequest("test2user")

        when:
        profileService.modifyLoginId(givenRequest, 1L)

        then:
        1 * userRepository.findById(*_) >> Optional.empty()
        def e = thrown(CustomException)
        e.errorCode == ErrorCode.NOT_FOUND
    }

    def "modifyPhoneNumber - userRepository 를 호출한다"() {
        given:
        def givenRequest = new ProfilePhoneNumberRequest("01012345678")

        when:
        profileService.modifyPhoneNumber(givenRequest, 1L)

        then:
        1 * userRepository.findById(*_) >> {
            Long userId ->
                userId == 1L
                Optional.of(User.builder().build())
        }
    }

    def "modifyPhoneNumber - userId 에 해당하는 사용자가 없으면 예외를 던진다"() {
        given:
        def givenRequest = new ProfilePhoneNumberRequest("01012345678")

        when:
        profileService.modifyPhoneNumber(givenRequest, 1L)

        then:
        1 * userRepository.findById(*_) >> Optional.empty()
        def e = thrown(CustomException)
        e.errorCode == ErrorCode.NOT_FOUND
    }

    def "modifyEmail - userRepository, signupManager 를 호출한다"() {
        given:
        def givenRequest = new ProfileEmailRequest("test2user@naver.com")

        when:
        profileService.modifyEmail(givenRequest, 1L)

        then:
        1 * userRepository.findById(*_) >> {
            Long userId ->
                userId == 1L
                Optional.of(User.builder().build())
        }
        and:
        1 * signupManager.isEmailDuplicated(*_)
    }

    def "modifyEmail - userId 에 해당하는 사용자가 없으면 예외를 던진다"() {
        given:
        def givenRequest = new ProfileEmailRequest("test2user@naver.com")

        when:
        profileService.modifyEmail(givenRequest, 1L)

        then:
        1 * userRepository.findById(*_) >> Optional.empty()
        def e = thrown(CustomException)
        e.errorCode == ErrorCode.NOT_FOUND
    }

    def "modifyPassword - userRepository, passwordProcessor 를 호출한다"() {
        given:
        def givenRequest = new ProfilePasswordRequest("1234")

        when:
        profileService.modifyPassword(givenRequest, 1L)

        then:
        1 * userRepository.findById(*_) >> {
            Long userId ->
                userId == 1L
                Optional.of(User.builder().build())
        }
        and:
        1 * passwordProcessor.encode(*_) >> {
            String password ->
                password == givenRequest.password()
                "dfienfkef"
        }
    }

    def "modifyPassword - userId 에 해당하는 사용자가 없으면 예외를 던진다"() {
        given:
        def givenRequest = new ProfilePasswordRequest("1234")

        when:
        profileService.modifyPassword(givenRequest, 1L)

        then:
        1 * userRepository.findById(*_) >> Optional.empty()
        def e = thrown(CustomException)
        e.errorCode == ErrorCode.NOT_FOUND
    }


}
