package kr.co.goldenhome.implement;

import kr.co.goldenhome.FcmManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationSender {

//    private final UserApi userApi;
    private final FcmManager fcmManager;

    public void send() {

    }
}
