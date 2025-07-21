package kr.co.goldenhome.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.FileUploadRequest
import kr.co.goldenhome.dto.FileUploadResponse
import kr.co.goldenhome.service.FileUploadService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class FileUploadControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    FileUploadService fileUploadService = Mock()

    def "pre-signed url 발급"() {
        given:
        def request = new FileUploadRequest(List.of("file1.jpg"))
        def expectedResponse = new FileUploadResponse("formatted-file-name-123.jpg", "https://mock-presigned-url.s3.amazonaws.com/formatted-file-name-123.jpg?X-Amz-Signature")
        fileUploadService.createPresignedUrlsForUpload(request) >> List.of(expectedResponse)

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/files")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
            MockMvcResultMatchers.jsonPath('$[0].formattedFileName').value(expectedResponse.formattedFileName())
            MockMvcResultMatchers.jsonPath('$[0].presignedUrl').value(expectedResponse.presignedUrl())
        }
    }

}
