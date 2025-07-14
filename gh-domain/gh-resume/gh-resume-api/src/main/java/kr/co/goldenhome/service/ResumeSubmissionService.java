package kr.co.goldenhome.service;

import kr.co.goldenhome.repository.ResumeSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeSubmissionService {

    private final ResumeSubmissionRepository resumeSubmissionRepository;

    public void create() {

    }
}
