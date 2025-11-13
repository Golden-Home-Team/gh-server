package kr.co.goldenhome;

import com.google.firebase.messaging.*;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmManager {

    private final FirebaseMessaging firebaseMessaging;
//
//    public void sendMessage(NotificationRequest request) {
//        Notification notification = Notification.builder()
//                .setTitle(request.title())
//                .setBody(request.body())
//                .build();
//        Message message = Message.builder()
//                .setToken(request.token())
//                .setNotification(notification)
//                .setApnsConfig(getApnsConfig(request))
//                .setAndroidConfig(getAndroidConfig())
//                .build();
//        try {
//            firebaseMessaging.sendAsync(message);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw new CustomException(ErrorCode.FCM_FAILED, "FcmSender.sendMessage");
//        }
//    }


    public void subscribeToTopic(List<String> tokens, String topicName) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .subscribeToTopic(tokens, topicName);
            if (response.getFailureCount() > 0) {
                // 실패한 토큰처리

            }
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void unsubscribeFromTopic(List<String> tokens, String topicName) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(tokens, topicName);

            if (response.getFailureCount() > 0) {
                // 실패한 토큰처리
            }
        } catch (Exception e) {
            // 예외 처리
        }
    }

    public void sendMessages(NotificationsRequest request) {
        Notification notification = Notification.builder()
                .setTitle(request.title())
                .setBody(request.body())
                .build();
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(notification)
                .setApnsConfig(getApnsConfig(request))
                .setAndroidConfig(getAndroidConfig(request))
                .addAllTokens(request.token())
                .build();
        try {
            firebaseMessaging.sendEachForMulticastAsync(message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomException(ErrorCode.FCM_FAILED, "FcmSender.sendMessages");
        }
    }

    /**
     * https://developer.apple.com/documentation/usernotifications/generating-a-remote-notification
     */
    private ApnsConfig getApnsConfig(NotificationsRequest request) {
        return ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setAlert(ApsAlert
                                .builder()
                                .setTitle(request.title())
                                .setBody(request.body())
//                                .setLaunchImage()
                                .build())
                        .build())
                .build();
    }

    /**
     * https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages?_gl=1*rfaly5*_up*MQ..*_ga*MTIzMDg1MTE1NC4xNzYyODg4ODQw*_ga_CW55HF8NVT*czE3NjI4ODg4MzkkbzEkZzAkdDE3NjI4ODg4NzMkajI2JGwwJGgw#AndroidNotification
     */
    private AndroidConfig getAndroidConfig(NotificationsRequest request) {
        return AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH) // 자원사용 증가, 알림 빨리 도달
                .setNotification(AndroidNotification.builder()
                        .setTitle(request.title())
                        .setBody(request.body())
                        .build())
                .build();
    }
}
