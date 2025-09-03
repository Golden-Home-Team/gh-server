package kr.co.goldenhome.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.goldenhome.FacilityEvent;
import kr.co.goldenhome.entity.FacilityEventLog;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.repository.FacilityEventLogRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import software.amazon.awssdk.services.sns.SnsAsyncClient;

import java.util.List;

@Component
public class ViewEventManger extends EventManager<FacilityEvent>{

    private final FacilityEventLogRepository facilityEventLogRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ViewEventManger(SnsAsyncClient snsAsyncClient,
                           EventUtils eventUtils,
                           FacilityEventLogRepository facilityEventLogRepository,
                           ApplicationEventPublisher applicationEventPublisher1) {
        super(snsAsyncClient, eventUtils);
        this.facilityEventLogRepository = facilityEventLogRepository;
        this.applicationEventPublisher = applicationEventPublisher1;
    }

    @Transactional
    @Override
    public void saveLog(FacilityEvent event) throws JsonProcessingException {
        facilityEventLogRepository.save(FacilityEventLog.create(event.getEventId(), eventUtils.toJson(event)));
        applicationEventPublisher.publishEvent(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void publish(FacilityEvent event) throws JsonProcessingException {
        snsAsyncClient.publish(eventUtils.createPublishRequest(snsTopicArn, event))
                .thenAcceptAsync(publishResponse -> {
                    facilityEventLogRepository.publish(event.getEventId());
                });
    }

    @Override
    public List<FacilityEvent> getUnpublishedEvents() {
        return facilityEventLogRepository.getUnpublished().stream().map(facilityEventLog -> {
            try {
                return eventUtils.fromJson(facilityEventLog.getPayload());
            } catch (JsonProcessingException e) {
                throw new CustomException(ErrorCode.UNKNOWN_ERROR, "ViewEventManger.getUnpublishedEvents");
            }
        }).toList();
    }

    @Override
    public void markAsPublished(List<String> eventIds) {
        facilityEventLogRepository.publish(eventIds);
    }
}
