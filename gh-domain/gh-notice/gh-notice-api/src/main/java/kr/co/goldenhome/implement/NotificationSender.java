package kr.co.goldenhome.implement;

import kr.co.goldenhome.FcmSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationSender {

//    private final UserApi userApi;
    private final FcmSender fcmSender;

    public void send() {

    }
}
