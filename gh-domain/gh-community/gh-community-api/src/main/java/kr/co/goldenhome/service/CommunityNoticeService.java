package kr.co.goldenhome.service;

import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.dto.CommunityNoticeRequest;
import kr.co.goldenhome.dto.CommunityNoticeUpdateRequest;
import kr.co.goldenhome.entity.CommunityNotice;
import kr.co.goldenhome.repository.CommunityNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityNoticeService {

    private final CommunityNoticeRepository communityNoticeRepository;

    public void create(CommunityNoticeRequest request, Long facilityId) {
        communityNoticeRepository.save(CommunityNotice.create(request.title(), request.content(), facilityId));
    }

    @Transactional
    public void update(CommunityNoticeUpdateRequest request, Long noticeId) {
        CommunityNotice communityNotice = communityNoticeRepository.findById(noticeId).orElseThrow(() -> new CustomException(ErrorCode.NOTICE_NOT_FOUND, "CommunityNoticeService.update"));
        communityNotice.update(request.title(), request.content());
    }

    public CommunityNotice read(Long noticeId) {
        return communityNoticeRepository.findById(noticeId).orElseThrow(()-> new CustomException(ErrorCode.NOTICE_NOT_FOUND, "CommunityNoticeService.read"));
    }

    public Optional<CommunityNotice> readLatest(Long facilityId) {
        return communityNoticeRepository.findTopByFacilityIdOrderByCreatedAtDesc(facilityId);
    }

    public List<CommunityNotice> readAll(Long facilityId, Long lastId, Long pageSize) {
        return lastId == null ?
                communityNoticeRepository.findAllInfiniteScroll(facilityId, pageSize) :
                communityNoticeRepository.findAllInfiniteScroll(facilityId, lastId, pageSize);
    }
}
