package kr.co.goldenhome.implement;

import kr.co.goldenhome.entity.DailyDiet;
import kr.co.goldenhome.repository.DailyDietRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DailyDietAppender {

    private final DailyDietRepository dailyDietRepository;

    public Long save(Long facilityId, String content, LocalDate recordDate) {
        return dailyDietRepository.save(DailyDiet.create(facilityId, content, recordDate)).getId();
    }
}
