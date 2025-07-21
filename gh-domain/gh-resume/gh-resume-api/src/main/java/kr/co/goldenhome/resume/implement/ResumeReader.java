package kr.co.goldenhome.resume.implement;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.entity.Resume;
import kr.co.goldenhome.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResumeReader {

    private final ResumeRepository resumeRepository;

    public Resume read(Long userId) {
        return resumeRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "ResumeService.readBaseResume"));
    }
}
