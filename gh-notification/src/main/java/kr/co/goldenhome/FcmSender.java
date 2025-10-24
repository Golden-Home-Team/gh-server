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
            throw new CustomException(ErrorCode.FCM_FAILED, "FcmSender.sendMessage");
        }
    }

    public void sendMessages(NotificationsRequest request) {
        Notification notification = Notification.builder()
                .setTitle(request.title())
                .setBody(request.body())
                .build();
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(notification)
                .setApnsConfig(getApnsConfig())
                .setAndroidConfig(getAndroidConfig())
                .addAllTokens(request.token())
                .build();
        try {
            firebaseMessaging.sendEachForMulticastAsync(message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomException(ErrorCode.FCM_FAILED, "FcmSender.sendMessages");
        }
    }

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
