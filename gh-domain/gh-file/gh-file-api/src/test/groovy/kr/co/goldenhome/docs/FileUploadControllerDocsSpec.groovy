package kr.co.goldenhome.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.FileUploadRequest
import kr.co.goldenhome.dto.FileUploadResponse
import kr.co.goldenhome.service.FileUploadService
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class FileUploadControllerDocsSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    FileUploadService fileUploadService = Mock()

    def "pre-signed url 발급"() {
        given:
        def request = new FileUploadRequest(List.of("파일1"))
        def expectedResponse = new FileUploadResponse("formatted-file-name-123.jpg", "https://mock-presigned-url.s3.amazonaws.com/formatted-file-name-123.jpg?X-Amz-Signature")
        fileUploadService.createPresignedUrlsForUpload(request) >> List.of(expectedResponse)

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/files")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("get-pre-signed-url-for-upload",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("fileNames").type(JsonFieldType.ARRAY)
                                        .description("업로드할 파일 이름 목록 (확장자 포함)")
                        ),
                        responseFields(
                                fieldWithPath("[].formattedFileName").type(JsonFieldType.STRING)
                                        .description("업로드될 파일의 포맷된 이름 (예: UUID.확장자)"),
                                fieldWithPath("[].presignedUrl").type(JsonFieldType.STRING)
                                        .description("파일을 업로드할 때 사용할 Presigned URL")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$[0].formattedFileName').value(expectedResponse.formattedFileName())
            MockMvcResultMatchers.jsonPath('$[0].presignedUrl').value(expectedResponse.presignedUrl())
        }
    }

}
