package kr.co.goldenhome.service;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.dto.CommunityNoticeRequest;
import kr.co.goldenhome.entity.CommunityNotice;
import kr.co.goldenhome.repository.CommunityNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityNoticeService {

    private final CommunityNoticeRepository communityNoticeRepository;

    public void create(CommunityNoticeRequest request, Long facilityId, Long userId) {
        communityNoticeRepository.save(CommunityNotice.create(request.title(), request.content(), facilityId, userId));
    }

    public CommunityNotice read(Long noticeId) {
        return communityNoticeRepository.findById(noticeId).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND, "CommunityNoticeService.read"));
    }

    public List<CommunityNotice> readAll(Long facilityId, Long lastId, Long pageSize) {
        return lastId == null ?
                communityNoticeRepository.findAllInfiniteScroll(facilityId, pageSize) :
                communityNoticeRepository.findAllInfiniteScroll(facilityId, lastId, pageSize);
    }
}
