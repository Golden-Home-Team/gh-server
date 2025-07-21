package kr.co.goldenhome.submission.service;


import kr.co.goldenhome.submission.dto.ResumeSubmissionModifyRequest;
import kr.co.goldenhome.submission.dto.ResumeSubmissionResponse;
import kr.co.goldenhome.entity.ResumeSubmission;
import kr.co.goldenhome.submission.implement.ResumeSubmissionModifier;

import kr.co.goldenhome.submission.implement.ResumeSubmissionReader;
import kr.co.goldenhome.submission.implement.ResumeSubmitter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeSubmissionService {

    private final ResumeSubmissionReader resumeSubmissionReader;
    private final ResumeSubmitter resumeSubmitter;
    private final ResumeSubmissionModifier resumeSubmissionModifier;

    public void submit(Long facilityId, Long userId) {
        resumeSubmitter.submit(facilityId, userId);
    }

    public ResumeSubmissionResponse read(Long resumeSubmissionId, Long userId) {
        return resumeSubmissionReader.read(resumeSubmissionId, userId);
    }

    public List<ResumeSubmissionResponse> readAll(Long userId, Long lastId, Long pageSize) {
        List<ResumeSubmission> resumeSubmissions = resumeSubmissionReader.readAll(userId, lastId, pageSize);
        return resumeSubmissions.stream().map(ResumeSubmissionResponse::from).toList();
    }

    public void modify(ResumeSubmissionModifyRequest request, Long resumeSubmissionId, Long userId) {
        resumeSubmissionModifier.modify(request, resumeSubmissionId, userId);
    }
}
