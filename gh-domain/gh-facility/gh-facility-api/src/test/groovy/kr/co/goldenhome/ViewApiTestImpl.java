package kr.co.goldenhome;

import org.springframework.stereotype.Component;

@Component
public class ViewApiTestImpl implements ViewApi {
    @Override
    public Long increase(Long facilityId, Long userId) {
        return 0L;
    }
}
