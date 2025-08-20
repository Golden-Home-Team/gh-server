package kr.co.goldenhome.implement;

import kr.co.goldenhome.entity.DailyShot;
import kr.co.goldenhome.repository.DailyShotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DailyShotAppender {

    private final DailyShotRepository dailyShotRepository;

    public Long save(Long facilityId, String content, LocalDate recordDate) {
        return dailyShotRepository.save(DailyShot.create(facilityId, content, recordDate)).getId();
    }
}
