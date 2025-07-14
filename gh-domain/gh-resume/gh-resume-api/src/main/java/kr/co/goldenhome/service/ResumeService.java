package kr.co.goldenhome.service;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.dto.ResumeModifyRequest;
import kr.co.goldenhome.dto.ResumeResponse;
import kr.co.goldenhome.entity.Resume;
import kr.co.goldenhome.dto.ResumeCreateRequest;
import kr.co.goldenhome.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public void create(ResumeCreateRequest request, Long userId) {
        resumeRepository.save(
                Resume.create(
                        userId,
                        request.name(),
                        request.dateOfBirth(),
                        request.gender(),
                        request.longTermCareGrade(),
                        request.majorDiseases(),
                        request.specialNotes(),
                        request.guardianName(),
                        request.guardianContactInformation(),
                        request.relationShip()
                )
        );
    }

    public ResumeResponse read(Long userId) {
        Resume resume = resumeRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ResumeService.readBaseResume"));
        return ResumeResponse.from(resume);
    }

    @Transactional
    public void modify(ResumeModifyRequest request, Long userId) {
        Resume resume = resumeRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ResumeService.readBaseResume"));
        resume.update(request.name(), request.dateOfBirth(), request.gender(), request.longTermCareGrade(), request.majorDiseases(), request.specialNotes(), request.guardianName(), request.guardianContactInformation(), request.relationShip());
    }
}
