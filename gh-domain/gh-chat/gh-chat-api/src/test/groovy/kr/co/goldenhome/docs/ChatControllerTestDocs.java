package kr.co.goldenhome.docs;

import kr.co.goldenhome.controller.ChatController;
import kr.co.goldenhome.dto.ChatMessageResponse;
import kr.co.goldenhome.dto.ChatRoomMetadataRepositoryResponse;
import kr.co.goldenhome.dto.ChatRoomResponse;
import kr.co.goldenhome.dto.SliceResponse;
import kr.co.goldenhome.enums.ChatRoomType;
import kr.co.goldenhome.messaging.SessionAttributeAccessor;
import kr.co.goldenhome.service.ChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class ChatControllerTestDocs extends RestDocsSupport{

    private final ChatService chatService = mock(ChatService.class);
    private final SessionAttributeAccessor sessionAttributeAccessor = mock(SessionAttributeAccessor.class);

    @Override
    protected Object initController() {
        return new ChatController(chatService, sessionAttributeAccessor);
    }

    @Test
    void enter() throws Exception {
        given(chatService.enterRoom(any(), any()))
                .willReturn(1L);
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/chat/{facilityId}", 1L))
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
                )).andReturn().getResponse().getContentAsString();

        System.out.println("response = " + response);
    }

    @Test
    void getChatRoom() throws Exception {
        Long expectedChatRoomId = 1L;
        Long expectedFacilityId = 1L;
        ChatRoomType expectedChatRoomType = ChatRoomType.DIRECT_MESSAGE;
        LocalDateTime expectedCreatedAt = LocalDateTime.of(2024, 10, 10, 19, 1);
        String expectedLastMessage = "HI";
        String expectedTimestamp = LocalDateTime.of(2024, 10, 10, 19, 2).toString();
        boolean expectedHasNext = false;
        int expectedNumberOfElements = 10;
        LocalDateTime expectedNextCursor = LocalDateTime.now().minusHours(1L);
        SliceResponse<ChatRoomMetadataRepositoryResponse> sliceResponse = new SliceResponse<>(
                List.of(new ChatRoomMetadataRepositoryResponse(expectedChatRoomId, expectedFacilityId, expectedChatRoomType, expectedCreatedAt, expectedLastMessage, expectedTimestamp)),
                expectedHasNext,
                expectedNumberOfElements,
                expectedNextCursor
        );
        given(chatService.getChatRooms(any(), any(), anyInt()))
                .willReturn(sliceResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/api/chat")
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
                                fieldWithPath("content[].createdAt").type(JsonFieldType.ARRAY)
                                        .description("채팅방 생성일"),
                                fieldWithPath("content[].lastMessage").type(JsonFieldType.STRING)
                                        .description("마지막 메시지"),
                                fieldWithPath("content[].timestamp").type(JsonFieldType.STRING)
                                        .description("마지막 메시지 생성시간"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                        .description("다음 페이지 존재 여부"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("응답한 본문 크기"),
                                fieldWithPath("cursor").type(JsonFieldType.ARRAY)
                                        .description("다음 페이지를 조회 하기 위한 커서")
                                )
                )).andReturn().getResponse().getContentAsString();

        System.out.println("response = " + response);
    }

    @Test
    void getChatMessages() throws Exception {
        Long givenChatRoomId = 1L;
        Long expectedSenderId = 1L;
        boolean expectedIsMine = false;
        String expectedMessage = "HI";
        String expectedTimestamp = LocalDateTime.of(2024, 10, 10, 19, 2).toString();
        boolean expectedHasNext = false;
        int expectedNumberOfElements = 10;
        LocalDateTime expectedNextCursor = LocalDateTime.now().minusHours(1L);
        SliceResponse<ChatMessageResponse> sliceResponse = new SliceResponse<>(
                List.of(new ChatMessageResponse(expectedSenderId, expectedIsMine, expectedMessage,expectedTimestamp)),
                expectedHasNext,
                expectedNumberOfElements,
                expectedNextCursor
        );
        given(chatService.getChatMessages(any(), any(), any(), anyInt()))
        .willReturn(sliceResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/api/chat/{chatRoomId}", givenChatRoomId)
                        .queryParam("cursor", LocalDateTime.now().toString())
                        .queryParam("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("chat-get-messages",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("chatRoomId").description("채팅방 아이디")
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
                                fieldWithPath("cursor").type(JsonFieldType.ARRAY)
                                        .description("다음 페이지를 조회 하기 위한 커서")
                        )
                )).andReturn().getResponse().getContentAsString();
        System.out.println("response = " + response);

    }


}
