package kr.co.goldenhome.docs

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.goldenhome.dto.ChatMessageResponse
import kr.co.goldenhome.dto.ChatRoomMetadataRepositoryResponse
import kr.co.goldenhome.dto.SliceResponse
import kr.co.goldenhome.enums.ChatRoomType
import kr.co.goldenhome.service.ChatService
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

import java.time.LocalDateTime

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ChatControllerSpecDocs extends Specification{

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    ChatService chatService = Mock()

    def "채팅방을 생성하거나 가져온다"() {
        given:
        chatService.enterRoom(*_) >> 1L
        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/api/chat/{facilityId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("chat-enter",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("facilityId").description("시설 아이디")
                        ),
                        responseFields(
                                fieldWithPath("chatRoomId").type(JsonFieldType.NUMBER)
                                        .description("채팅방 아이디")
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }

    def "채팅방 목록 가져오기"() {
        given:
        def expectedChatRoomId = 1L
        def expectedFacilityId = 1L
        def expectedChatRoomType = ChatRoomType.DIRECT_MESSAGE
        def expectedCreatedAt = LocalDateTime.of(2024, 10, 10, 19, 1)
        def expectedLastMessage = "HI"
        def expectedTimestamp = LocalDateTime.of(2024, 10, 10, 19, 2).toString()
        def expectedHasNext = false
        def expectedNumberOfElements = 10
        def expectedNextCursor = LocalDateTime.now().minusHours(1L)
        def expectedResponse = new SliceResponse(
                List.of(new ChatRoomMetadataRepositoryResponse(expectedChatRoomId, expectedFacilityId, expectedChatRoomType, expectedCreatedAt, expectedLastMessage, expectedTimestamp)),
                expectedHasNext,
                expectedNumberOfElements,
                expectedNextCursor
        )
        chatService.getChatRooms(*_) >> expectedResponse
        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/chat")
        .queryParam("cursor", LocalDateTime.now().toString())
        .queryParam("pageSize", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("chat-get-rooms",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("cursor").description("다음 페이지 커서"),
                                parameterWithName("pageSize").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("content").type(JsonFieldType.ARRAY)
                                        .description("페이징 처리된 본문"),
                                fieldWithPath("content[].chatRoomId").type(JsonFieldType.NUMBER)
                                        .description("채팅방 아이디"),
                                fieldWithPath("content[].facilityId").type(JsonFieldType.NUMBER)
                                        .description("시설 아이디"),
                                fieldWithPath("content[].chatRoomType").type(JsonFieldType.STRING)
                                        .description("채팅방 타입"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING)
                                        .description("채팅방 생성일"),
                                fieldWithPath("content[].lastMessage").type(JsonFieldType.STRING)
                                        .description("마지막 메시지"),
                                fieldWithPath("content[].timestamp").type(JsonFieldType.STRING)
                                        .description("마지막 메시지 생성시간"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                        .description("다음 페이지 존재 여부"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("응답한 본문 크기"),
                                fieldWithPath("cursor").type(JsonFieldType.STRING)
                                        .description("다음 페이지를 조회 하기 위한 커서"),
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }

    def "채팅방 메시지 가져오기"() {
        given:
        def givenChatRoomId = 1L
        def expectedSenderId = 1L
        def expectedIsMine = false
        def expectedMessage = "HI"
        def expectedTimestamp = LocalDateTime.of(2024, 10, 10, 19, 2).toString()
        def expectedHasNext = false
        def expectedNumberOfElements = 10
        def expectedNextCursor = LocalDateTime.now().minusHours(1L)
        def expectedResponse = new SliceResponse(
                List.of(new ChatMessageResponse(expectedSenderId, expectedIsMine, expectedMessage,expectedTimestamp)),
                expectedHasNext,
                expectedNumberOfElements,
                expectedNextCursor
        )
        chatService.getChatMessages(*_) >> expectedResponse
        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/api/chat/{chatRoomId}", givenChatRoomId)
                .queryParam("cursor", LocalDateTime.now().toString())
                .queryParam("pageSize", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("chat-get-messages",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("chatRoomId").description("채팅방 아이디"),
                        ),
                        queryParameters(
                                parameterWithName("cursor").description("다음 페이지 커서"),
                                parameterWithName("pageSize").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("content").type(JsonFieldType.ARRAY)
                                        .description("페이징 처리된 본문"),
                                fieldWithPath("content[].senderId").type(JsonFieldType.NUMBER)
                                        .description("메시지를 보낸 사용자 아이디"),
                                fieldWithPath("content[].isMine").type(JsonFieldType.BOOLEAN)
                                        .description("보낸 메시지주인 본인여부"),
                                fieldWithPath("content[].message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("content[].timestamp").type(JsonFieldType.STRING)
                                        .description("메시지 전송시간"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                        .description("다음 페이지 존재 여부"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("응답한 본문 크기"),
                                fieldWithPath("cursor").type(JsonFieldType.STRING)
                                        .description("다음 페이지를 조회 하기 위한 커서"),
                        )
                ))

        then:
        response.andExpect {
            MockMvcResultMatchers.status().isOk()
        }
    }

}
