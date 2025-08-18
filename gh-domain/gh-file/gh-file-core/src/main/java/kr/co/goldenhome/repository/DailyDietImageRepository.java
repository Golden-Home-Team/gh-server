package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.DailyDietImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DailyDietImageRepository extends JpaRepository<DailyDietImage, Long> {
    void deleteAllByDailyDietId(Long dailyDietId);
    Optional<DailyDietImage> findTopByDailyDietIdOrderByCreatedAtDesc(Long dailyDietId);
    List<DailyDietImage> findByDailyDietId(Long dailyDietId);
}
