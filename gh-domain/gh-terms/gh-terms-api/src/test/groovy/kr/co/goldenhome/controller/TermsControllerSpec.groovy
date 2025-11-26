package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.auth.UserPrincipal
import kr.co.goldenhome.dto.TermsAgreementRequest
import kr.co.goldenhome.dto.TermsRequest
import kr.co.goldenhome.dto.TermsResponse
import kr.co.goldenhome.entity.Terms
import kr.co.goldenhome.entity.TermsType
import kr.co.goldenhome.service.TermsService
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

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class TermsControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    TermsService termsService = Mock()

    def "약관 생성"() {
        given:
        def givenRequest = new TermsRequest(TermsType.LOCATION_SERVICE_TERMS.name(), "1.0", "title", "content", false)

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/terms")
                .principal(new UserPrincipal(1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest)))
                .andDo(document("terms-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("약관 제목(관리자만 가능한 API)"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("약관 내용"),
                                fieldWithPath("termsType").type(JsonFieldType.STRING)
                                        .description("약관 종류 e.g. SERVICE_USAGE_TERMS(\"서비스 이용약관\"),\n" +
                                                "    PRIVACY_POLICY(\"개인정보 처리방침\"),\n" +
                                                "    LOCATION_SERVICE_TERMS(\"위치기반 서비스 이용약관\");"),
                                fieldWithPath("version").type(JsonFieldType.STRING)
                                        .description("약관 버전"),
                                fieldWithPath("isMandatory").type(JsonFieldType.BOOLEAN)
                                        .description("약관 필수 여부")
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

    def "약관 조회"() {
        given:
        termsService.getActiveTerms() >> List.of(Terms.builder().id(1).termsType(TermsType.LOCATION_SERVICE_TERMS).title("title").content("content").isMandatory(true).version("1.1v").build())

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/terms")
                .principal(new UserPrincipal(1L))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("terms-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].termsType").type(JsonFieldType.STRING)
                                        .description("약관 종류"),
                                fieldWithPath("[].version").type(JsonFieldType.STRING)
                                        .description("약관 버전"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING)
                                        .description("약관 제목"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING)
                                        .description("약관 내용"),
                                fieldWithPath("[].isMandatory").type(JsonFieldType.BOOLEAN)
                                        .description("약관 필수 여부"),

                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }

    def "약관 동의"() {
        given:
        def givenRequest = new TermsAgreementRequest(List.of(new TermsAgreementRequest.TermsItem(1, true)))

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/terms/agree")
                .principal(new UserPrincipal(1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(givenRequest)))
                .andDo(document("terms-agree",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("agreements").type(JsonFieldType.ARRAY)
                                        .description("약관별 동의여부 목록"),
                                fieldWithPath("agreements[].termsId").type(JsonFieldType.NUMBER)
                                        .description("약관 아이디"),
                                fieldWithPath("agreements[].isAgreed").type(JsonFieldType.BOOLEAN)
                                        .description("약관 동의여부"),
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
