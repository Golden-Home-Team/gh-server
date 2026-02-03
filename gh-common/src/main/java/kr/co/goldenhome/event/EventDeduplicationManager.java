package kr.co.goldenhome.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EventDeduplicationManager {

    private final EventDeduplicationLogRepository eventDeduplicationLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(String id) {
        eventDeduplicationLogRepository.saveAndFlush(EventDeduplicationLog.create(id));
    }
}
