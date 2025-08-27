package kr.co.goldenhome;

import org.springframework.stereotype.Component;

@Component
public class UserApiTestImpl implements UserApi {
    @Override
    public String getLoginId(Long userId) {
        return "";
    }

    @Override
    public String getUserName(Long userId) {
        return "";
    }
}
