package kr.co.goldenhome;

import com.google.firebase.messaging.*;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmSender {

    private final FirebaseMessaging firebaseMessaging;

    // https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages?_gl=1*wck368*_up*MQ..*_ga*ODQ4NjQxNzY3LjE3NjAzNzk5MTE.*_ga_CW55HF8NVT*czE3NjAzNzk5MTEkbzEkZzAkdDE3NjAzNzk5MTEkajYwJGwwJGgw#Notification
    public void sendMessage(NotificationRequest request) {
        Notification notification = Notification.builder()
                .setTitle(request.title())
                .setBody(request.body())
                .build();
        Message message = Message.builder()
                .setToken(request.token())
                .setNotification(notification)
                .setApnsConfig(getApnsConfig())
                .setAndroidConfig(getAndroidConfig())
                .build();
        try {
            firebaseMessaging.sendAsync(message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomException(ErrorCode.FCM_FAILED, "FcmSender.send");
        }
    }

    public void sendMessages(NotificationRequest request) {
        MulticastMessage.builder(). //https://soso-hyeon.tistory.com/87
                build();
    }

    // https://developer.apple.com/documentation/usernotifications/sending-notification-requests-to-apns
    private ApnsConfig getApnsConfig() {
        return ApnsConfig.builder()
                .setAps(Aps.builder().build())
                .build();
    }

    private AndroidConfig getAndroidConfig() {
        return AndroidConfig.builder()
                .build();
    }
}
