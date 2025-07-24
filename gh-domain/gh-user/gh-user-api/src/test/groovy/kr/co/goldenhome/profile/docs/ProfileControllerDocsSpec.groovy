package kr.co.goldenhome.profile.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.ProfileImageApiResponse
import kr.co.goldenhome.profile.dto.ProfileImageRequest
import kr.co.goldenhome.profile.dto.ProfileResponse
import kr.co.goldenhome.profile.service.ProfileService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ProfileControllerDocsSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    ProfileService profileService = Mock()

    def "프로필 조회"() {

        given:
        def expectedProfileImageApiResponse = new ProfileImageApiResponse(1L, "123-abc-image1.jpg", "https://")
        def expectedResponse = new ProfileResponse(expectedProfileImageApiResponse, "구준형", "gucoding1234", "01012345678", "gucoding1234@naver.com")
        profileService.get(*_) >> expectedResponse

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/profiles"))
                .andDo(document("profile-get",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("profileImageApiResponse").type(JsonFieldType.OBJECT)
                                        .description("프로필이미지 정보 목록"),
                                fieldWithPath("profileImageApiResponse.id").type(JsonFieldType.NUMBER)
                                        .description("프로필이미지 아이디"),
                                fieldWithPath("profileImageApiResponse.formattedName").type(JsonFieldType.STRING)
                                        .description("프로필이미지 이름 (포맷 및 인코딩 됨)"),
                                fieldWithPath("profileImageApiResponse.imageUrl").type(JsonFieldType.STRING)
                                        .description("프로필이미지 주소"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("사용자 이름"),
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .description("로그인 아이디"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                                        .description("전화번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$[0].profileImageApiResponse[0].id').value(1L)
            MockMvcResultMatchers.jsonPath('$[0].profileImageApiResponse[0].formattedName').value("123-abc-image1.jpg")
            MockMvcResultMatchers.jsonPath('$[0].profileImageApiResponse[0].imageUrl').value("https://")
            MockMvcResultMatchers.jsonPath('$.name').value("구준형")
            MockMvcResultMatchers.jsonPath('$.loginId').value("gucoding1234")
            MockMvcResultMatchers.jsonPath('$.phoneNumber').value("01012345678")
            MockMvcResultMatchers.jsonPath('$.email').value("gucoding1234@naver.com")
        }
    }

    def "프로필 이미지 생성"() {
        given:
        def givenRequest = new ProfileImageRequest("123-abc-image1.jpg")

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest)))
                .andDo(document("profile-image-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("formattedImageName").type(JsonFieldType.STRING)
                                        .description("프로필이미지 이름 (포맷 및 인코딩 됨)"),
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$.success').value(true)
        }
    }
}
