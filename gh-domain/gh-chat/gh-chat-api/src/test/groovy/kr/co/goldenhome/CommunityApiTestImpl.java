package kr.co.goldenhome;

import org.springframework.stereotype.Component;

@Component
public class CommunityApiTestImpl implements CommunityApi{
    @Override
    public Long getCommunityManagerUserId(Long facilityId) {
        return 0L;
    }
}
