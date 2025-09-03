package kr.co.goldenhome;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.goldenhome.event.EventDeduplicationLog;
import kr.co.goldenhome.event.EventDeduplicationLogRepository;
import kr.co.goldenhome.event.SqsMessage;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class DeduplicateEventAspect {

    private final EventDeduplicationLogRepository eventDeduplicationLogRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @Around("@annotation(kr.co.goldenhome.DeduplicateEvent)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String eventId = Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof SqsMessage)
                .map(arg -> (SqsMessage) arg)
                .findFirst()
                .map(sqsMessage -> {
                    try {
                        EventIdOnly eventIdOnly = objectMapper.readValue(sqsMessage.Message(), EventIdOnly.class);
                        return eventIdOnly.eventId();
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                })
                .orElse(null);
        if (eventId == null) {
            return joinPoint.proceed();
        }
        String id = eventId + "-" + joinPoint.getSignature().getName();
        if (eventDeduplicationLogRepository.existsById(id)) return null;
        Object result = joinPoint.proceed();
        eventDeduplicationLogRepository.save(EventDeduplicationLog.create(id));
        return result;
    }

}
