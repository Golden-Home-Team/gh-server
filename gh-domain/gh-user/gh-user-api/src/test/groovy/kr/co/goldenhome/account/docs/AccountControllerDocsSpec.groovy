package kr.co.goldenhome.account.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.account.dto.NotifySetting
import kr.co.goldenhome.account.service.AccountService
import kr.co.goldenhome.auth.UserPrincipal
import kr.co.goldenhome.authentication.dto.FcmRequest
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class AccountControllerDocsSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    AccountService accountService = Mock()

    def "회원탈퇴"() {

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/account/withdraw")
                .principal(new UserPrincipal(1L))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("user-withdraw",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공 여부")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }

    def "로그아웃"() {

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/account/logout")
                .principal(new UserPrincipal(1L))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("user-logout",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("로그아웃은 헤더에 액세스 토큰을 없애주세요. 해당 api 는 Refresh Token 을 없애는 용도입니다.")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }

    }

    def "FCM 토큰 저장"() {

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/account/fcm")
                .principal(new UserPrincipal(1L))
                .content(objectMapper.writeValueAsString(new FcmRequest("fcm-token", "device-id-1")))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("user-fcm",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("fcmToken").type(JsonFieldType.STRING)
                                        .description("fcm토큰"),
                                fieldWithPath("deviceId").type(JsonFieldType.STRING)
                                        .description("디바이스 아이디")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }

    }

    def "알림 설정"() {

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/account/notification")
                .principal(new UserPrincipal(1L))
                .content(objectMapper.writeValueAsString(new NotifySetting(true, false)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("user-notification",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("notice").type(JsonFieldType.BOOLEAN)
                                        .description("공지 알림 여부"),
                                fieldWithPath("chat").type(JsonFieldType.BOOLEAN)
                                        .description("채팅 알림 여부")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }
}
