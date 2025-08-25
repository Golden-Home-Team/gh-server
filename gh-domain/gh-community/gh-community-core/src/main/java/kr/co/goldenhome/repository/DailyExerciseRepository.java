package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.DailyExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyExerciseRepository extends JpaRepository<DailyExercise, Long> {
    List<DailyExercise> findAllByDailyRehabilitationId(Long dailyRehabilitationId);
}
