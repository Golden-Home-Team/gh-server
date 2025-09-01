package kr.co.goldenhome.event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventDeduplicationLogRepository extends JpaRepository<EventDeduplicationLog, String> {
}
