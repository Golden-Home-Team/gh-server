package kr.co.goldenhome.service;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.dto.CommunityInquiryRequest;
import kr.co.goldenhome.entity.CommunityInquiry;
import kr.co.goldenhome.enums.CommunityInquiryType;
import kr.co.goldenhome.repository.CommunityInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityInquiryService {

    private final CommunityInquiryRepository communityInquiryRepository;

    public void write(CommunityInquiryRequest request, Long facilityId, Long userId) {
        communityInquiryRepository.save(CommunityInquiry.create(facilityId, userId, request.recordDate(), request.content(), CommunityInquiryType.valueOf(request.type()), request.isUrgent()));
    }

    public CommunityInquiry read(Long inquiryId) {
        return communityInquiryRepository.findById(inquiryId).orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND, "CommunityInquiryService.read"));
    }

    public List<CommunityInquiry> readAll(Long facilityId, Long lastId, Long pageSize) {
        return lastId == null ?
                communityInquiryRepository.findAllInfiniteScroll(facilityId, pageSize) :
                communityInquiryRepository.findAllInfiniteScroll(facilityId, lastId, pageSize);
    }
}
