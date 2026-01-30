package kr.co.goldenhome;

import kr.co.goldenhome.event.EventDeduplicationLog;
import kr.co.goldenhome.event.EventDeduplicationLogRepository;
import kr.co.goldenhome.model.FacilityEvent;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@RequiredArgsConstructor
public class DeduplicateEventAspect {

    private final EventDeduplicationLogRepository eventDeduplicationLogRepository;

    @Transactional
    @Around("@annotation(kr.co.goldenhome.DeduplicateEvent)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        FacilityEvent event = (FacilityEvent) args[0];
        String eventId = event.getEventId();
        String id = eventId + "-" + joinPoint.getSignature().getName();
        if (eventDeduplicationLogRepository.existsById(id)) return null;
        Object result = joinPoint.proceed();
        eventDeduplicationLogRepository.save(EventDeduplicationLog.create(id));
        return result;
    }

}
