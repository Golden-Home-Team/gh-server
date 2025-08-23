package kr.co.goldenhome.service

import kr.co.goldenhome.FacilityProfileApi
import spock.lang.Specification

class FacilityCommandServiceSpec extends Specification {

    FacilityCommandService facilityCommandService
    FacilityProfileApi facilityProfileApi = Mock()

    def setup() {
        facilityCommandService = new FacilityCommandService(facilityProfileApi)
    }

    def "uploadProfile - facilityProfileApi 를 호출한다"() {
        given:
        def givenFacilityId = 1L
        def givenFormattedImageName = "abc-123.jpg"

        when:
        facilityCommandService.uploadProfile(givenFacilityId, givenFormattedImageName)

        then:
        1 * facilityProfileApi.save(*_) >> {
            Long facilityId, String formattedImageName ->
                facilityId == givenFacilityId
                formattedImageName == givenFormattedImageName
        }
    }
}
