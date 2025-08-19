package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.DailyShotImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DailyShotImageRepository extends JpaRepository<DailyShotImage, Long> {
    void deleteAllByDailyShotId(Long dailyShotId);
    List<DailyShotImage> findByDailyShotIdOrderByCreatedAtDesc(Long dailyShotId);

}
