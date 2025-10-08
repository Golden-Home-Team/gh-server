package kr.co.goldenhome.service

import kr.co.goldenhome.CommunityApi
import kr.co.goldenhome.dto.ChatRoomMetadataRepositoryResponse
import kr.co.goldenhome.dto.SliceResponse
import kr.co.goldenhome.entity.ChatMessage
import kr.co.goldenhome.entity.ChatRoom
import kr.co.goldenhome.enums.ChatRoomType
import kr.co.goldenhome.exception.CustomException
import kr.co.goldenhome.exception.ErrorCode
import kr.co.goldenhome.implement.ChatRoomManager
import kr.co.goldenhome.messaging.ChatMessagePublisher
import kr.co.goldenhome.messaging.ChatStreamWriter
import spock.lang.Specification

import java.time.LocalDateTime

class ChatServiceSpec extends Specification {

    ChatService chatService
    CommunityApi communityApi = Mock()
    ChatRoomManager chatRoomManager = Mock()
    ChatStreamWriter chatStreamWriter = Mock()
    ChatMessagePublisher chatMessagePublisher = Mock()

    def setup() {
        chatService = new ChatService(communityApi, chatRoomManager, chatStreamWriter, chatMessagePublisher)
    }

    def "enterRoom - communityApi, chatRoomManager 를 호출한다"(){
        given:
        def givenFacilityId = 1L
        def givenUserId = 1L
        def expectedManagerId = 2L
        def expectedChatRoom = ChatRoom.create(givenFacilityId, ChatRoomType.DIRECT_MESSAGE)

        when:
        chatService.enterRoom(givenFacilityId, givenUserId)

        then:
        1 * communityApi.getCommunityManagerUserId(givenFacilityId) >> expectedManagerId
        1 * chatRoomManager.getOrCreate(givenFacilityId,givenUserId,expectedManagerId) >>expectedChatRoom

    }

    def "enterRoom - 사용자와 커뮤니티 매니저 아이디가 같으면 예외를 던진다"(){
        given:
        def givenFacilityId = 1L
        def givenUserId = 1L
        def expectedManagerId = 1L

        when:
        chatService.enterRoom(givenFacilityId, givenUserId)

        then:
        1 * communityApi.getCommunityManagerUserId(givenFacilityId) >> expectedManagerId
        def e = thrown(CustomException)
        e.errorCode == ErrorCode.INVALID_CHAT_ROOM
    }

    def "chat - chatStreamWriter, chatMessagePublisher 를 호출한다"() {
        given:
        def givenChatRoomId = 1L
        def givenSenderId = 1L
        def givenContent = "hi"
        def givenChatMessage = ChatMessage.create(givenChatRoomId, givenSenderId, givenContent)

        when:
        chatService.chat(givenChatMessage)

        then:
        1 * chatStreamWriter.write(givenChatMessage)
        1 * chatMessagePublisher.sendChatMessage(givenChatMessage)
    }

    def "getChatRooms - chatRoomManager 을 호출한다"() {
        given:
        def givenCursor = LocalDateTime.now()
        def givenUserId = 1L
        def givenPageSize = 10
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

        when:
        chatService.getChatRooms(givenCursor, givenUserId, givenPageSize)

        then:
        1 * chatRoomManager.getChatRooms(givenCursor, givenUserId, givenPageSize) >> expectedResponse
    }

    def "getChatMessages - chatRoomManager 을 호출한다"() {
        given:
        def givenChatRoomId = 1L
        def givenCursor = LocalDateTime.now()
        def givenUserId = 1L
        def givenPageSize = 10

        when:
        chatService.getChatMessages(givenChatRoomId, givenCursor, givenUserId, givenPageSize)

        then:
        1 * chatRoomManager.getChatMessages(givenChatRoomId, givenCursor, givenUserId, givenPageSize)
    }


}
