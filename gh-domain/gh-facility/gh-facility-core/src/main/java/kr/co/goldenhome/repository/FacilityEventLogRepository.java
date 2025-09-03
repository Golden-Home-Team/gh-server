package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.FacilityEventLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FacilityEventLogRepository {

    private final FacilityEventLogJpaRepository jpaRepository;

    public void save(FacilityEventLog facilityEventLog) {
        jpaRepository.save(facilityEventLog);
    }

    @Transactional
    public void publish(String eventId) {
        jpaRepository.publish(eventId);
    }

    public List<FacilityEventLog> getUnpublished() {
        return jpaRepository.findByIsPublishedFalse();
    }

    @Transactional
    public void publish(List<String> eventIds) {
        jpaRepository.publish(eventIds);
    }
}
