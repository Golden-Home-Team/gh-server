package kr.co.goldenhome.resume.implement;

import kr.co.goldenhome.entity.Resume;
import kr.co.goldenhome.repository.ResumeRepository;
import kr.co.goldenhome.resume.dto.ResumeCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResumeWriter {

    private final ResumeRepository resumeRepository;

    public void write(ResumeCreateRequest request, Long userId) {
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
}
