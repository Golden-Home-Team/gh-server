package kr.co.goldenhome.resume.implement;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.entity.Resume;
import kr.co.goldenhome.repository.ResumeRepository;
import kr.co.goldenhome.resume.dto.ResumeModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ResumeModifier {

    private final ResumeRepository resumeRepository;

    @Transactional
    public void modify(ResumeModifyRequest request, Long userId) {
        Resume resume = resumeRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ResumeService.readBaseResume"));
        resume.update(request.name(), request.dateOfBirth(), request.gender(), request.longTermCareGrade(), request.majorDiseases(), request.specialNotes(), request.guardianName(), request.guardianContactInformation(), request.relationShip());
    }
}
