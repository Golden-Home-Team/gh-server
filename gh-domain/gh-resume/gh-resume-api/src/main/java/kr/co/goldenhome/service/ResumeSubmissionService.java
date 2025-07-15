package kr.co.goldenhome.service;


import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.dto.ResumeSubmissionModifyRequest;
import kr.co.goldenhome.dto.ResumeSubmissionResponse;
import kr.co.goldenhome.entity.ResumeSubmission;
import kr.co.goldenhome.impl.ResumeSubmissionManager;

import kr.co.goldenhome.repository.ResumeSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeSubmissionService {

    private final ResumeSubmissionManager resumeSubmissionManager;
    private final ResumeSubmissionRepository resumeSubmissionRepository;

    public void create(Long facilityId, Long userId) {
        resumeSubmissionManager.create(facilityId, userId);
    }

    public ResumeSubmissionResponse read(Long resumeSubmissionId, Long userId) {
        return resumeSubmissionManager.read(resumeSubmissionId, userId);
    }

    public List<ResumeSubmissionResponse> readAll(Long userId, Long lastId, Long pageSize) {
        List<ResumeSubmission> resumeSubmissions = lastId == null ?
                resumeSubmissionRepository.findAllInfiniteScroll(userId, pageSize) :
                resumeSubmissionRepository.findAllInfiniteScroll(userId, lastId, pageSize);
        return resumeSubmissions.stream().map(ResumeSubmissionResponse::from).toList();
    }

    public void modify(ResumeSubmissionModifyRequest request, Long resumeSubmissionId, Long userId) {
        resumeSubmissionManager.modify(request, resumeSubmissionId, userId);
    }
}
