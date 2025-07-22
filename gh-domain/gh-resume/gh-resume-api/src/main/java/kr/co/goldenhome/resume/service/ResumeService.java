package kr.co.goldenhome.resume.service;


import kr.co.goldenhome.resume.dto.ResumeModifyRequest;
import kr.co.goldenhome.resume.dto.ResumeResponse;
import kr.co.goldenhome.entity.Resume;
import kr.co.goldenhome.resume.dto.ResumeCreateRequest;
import kr.co.goldenhome.resume.implement.ResumeModifier;
import kr.co.goldenhome.resume.implement.ResumeReader;
import kr.co.goldenhome.resume.implement.ResumeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeWriter resumeWriter;
    private final ResumeReader resumeReader;
    private final ResumeModifier resumeModifier;

    public void write(ResumeCreateRequest request, Long userId) {
        resumeWriter.write(request, userId);
    }

    public ResumeResponse read(Long userId) {
        Resume resume = resumeReader.read(userId);
        return ResumeResponse.from(resume);
    }

    public void modify(ResumeModifyRequest request, Long userId) {
        resumeModifier.modify(request, userId);
    }
}
