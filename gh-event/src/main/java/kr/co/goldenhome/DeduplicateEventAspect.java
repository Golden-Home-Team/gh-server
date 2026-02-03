package kr.co.goldenhome;

import kr.co.goldenhome.event.EventDeduplicationManager;
import kr.co.goldenhome.model.FacilityEvent;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@RequiredArgsConstructor
public class DeduplicateEventAspect {

    private final EventDeduplicationManager eventDeduplicationManager;
    private static final Logger log = LoggerFactory.getLogger("api-history");

    @Transactional
    @Around("@annotation(kr.co.goldenhome.DeduplicateEvent)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        FacilityEvent event = (FacilityEvent) args[0];
        String eventId = event.getEventId();
        String id = eventId + "-" + joinPoint.getSignature().getName();
        try {
            eventDeduplicationManager.saveLog(id);
        } catch (DataIntegrityViolationException e) {
            log.info("Duplicate event detected and blocked: {}", id);
            return null;
        }
        return joinPoint.proceed();
    }

}
