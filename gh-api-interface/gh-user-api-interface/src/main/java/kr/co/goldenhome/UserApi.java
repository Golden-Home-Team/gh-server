package kr.co.goldenhome;

import java.util.List;

public interface UserApi {
    String getLoginId(Long userId);
    String getUserName(Long userId);
    List<String> getFcmTokens(List<Long> userIds);
}
