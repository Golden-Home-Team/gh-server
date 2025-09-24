package kr.co.goldenhome;

import kr.co.goldenhome.repository.CommunityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommunityApiImpl implements CommunityApi {

    private final CommunityUserRepository communityUserRepository;

    @Override
    public Long getCommunityManagerUserId(Long facultyId) {
        return communityUserRepository.getManager(facultyId).getUserId();
    }
}
