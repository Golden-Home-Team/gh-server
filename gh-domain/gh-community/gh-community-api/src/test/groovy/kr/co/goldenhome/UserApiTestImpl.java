package kr.co.goldenhome;

import org.springframework.stereotype.Component;

import java.util.List;

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

    @Override
    public List<String> getFcmTokens(List<Long> userIds, String type) {
        return List.of();
    }

}
