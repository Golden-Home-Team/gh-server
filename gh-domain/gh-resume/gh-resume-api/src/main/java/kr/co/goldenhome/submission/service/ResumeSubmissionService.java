package kr.co.goldenhome.submission.service;


import kr.co.goldenhome.FacilityApi;
import kr.co.goldenhome.FacilityApiResponse;
import kr.co.goldenhome.submission.dto.ResumeSubmissionModifyRequest;
import kr.co.goldenhome.submission.dto.ResumeSubmissionResponse;
import kr.co.goldenhome.entity.ResumeSubmission;
import kr.co.goldenhome.submission.implement.ResumeSubmissionModifier;

import kr.co.goldenhome.submission.implement.ResumeSubmissionReader;
import kr.co.goldenhome.submission.implement.ResumeSubmitter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeSubmissionService {

    private final ResumeSubmissionReader resumeSubmissionReader;
    private final ResumeSubmitter resumeSubmitter;
    private final ResumeSubmissionModifier resumeSubmissionModifier;
    private final FacilityApi facilityApi;

    public void submit(Long facilityId, Long userId) {
        resumeSubmitter.submit(facilityId, userId);
    }

    public ResumeSubmissionResponse read(Long resumeSubmissionId, Long userId) {
        ResumeSubmission resumeSubmission = resumeSubmissionReader.read(resumeSubmissionId, userId);
        FacilityApiResponse facilityApiResponse = facilityApi.get(resumeSubmission.getFacilityId());
        return ResumeSubmissionResponse.of(resumeSubmission, facilityApiResponse);
    }

    public List<ResumeSubmissionResponse> readAll(Long userId, Long lastId, Long pageSize) {
        List<ResumeSubmission> resumeSubmissions = resumeSubmissionReader.readAll(userId, lastId, pageSize);
        Map<Long, FacilityApiResponse> facilityMap = getFacilityApiResponseMap(resumeSubmissions);
        return getResponses(resumeSubmissions, facilityMap);
    }

    public void modify(ResumeSubmissionModifyRequest request, Long resumeSubmissionId, Long userId) {
        resumeSubmissionModifier.modify(request, resumeSubmissionId, userId);
    }

    private Map<Long, FacilityApiResponse> getFacilityApiResponseMap(List<ResumeSubmission> resumeSubmissions) {
        return resumeSubmissions.stream().map(ResumeSubmission::getFacilityId)
                .distinct()
                .collect(Collectors.toMap(
                        facilityId -> facilityId,
                        facilityApi::get
                ));
    }

    private static List<ResumeSubmissionResponse> getResponses(List<ResumeSubmission> resumeSubmissions, Map<Long, FacilityApiResponse> facilityMap) {
        return resumeSubmissions.stream().map(resumeSubmission -> {
            FacilityApiResponse facilityApiResponse = facilityMap.get(resumeSubmission.getFacilityId());
            return ResumeSubmissionResponse.of(resumeSubmission, facilityApiResponse);
        }).toList();
    }


}
