package validator

import kr.co.goldenhome.exception.CustomException
import kr.co.goldenhome.exception.ErrorCode
import kr.co.goldenhome.SocialPlatform
import kr.co.goldenhome.validator.EnumValidator
import spock.lang.Specification

class EnumValidatorTest extends Specification {

    def "존재하는 Enum"() {
        given:
        def existedEnumName = "KAKAO"

        when:
        EnumValidator.validate(SocialPlatform, "providerType", existedEnumName, "origin")

        then:
        noExceptionThrown()
    }

    def "존재하지않는 Enum"() {
        given:
        def existedEnumName = "PAPAO"
        def targetFieldName = "providerType"
        def origin = "origin"

        when:
        EnumValidator.validate(SocialPlatform, targetFieldName, existedEnumName, origin)

        then:
        CustomException e = thrown()
        e.getErrorCode() == ErrorCode.INVALID_ENUM
        e.getOrigin().contains(origin)
    }
}
