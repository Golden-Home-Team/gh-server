package kr.co.goldenhome.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.goldenhome.FacilityEvent;
import kr.co.goldenhome.entity.FacilityEventLog;
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
        System.out.println("ViewEventManger.saveLog.eventId = " + event.getEventId());
        facilityEventLogRepository.save(FacilityEventLog.create(event.getEventId(), eventUtils.toJson(event)));
        applicationEventPublisher.publishEvent(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void publish(FacilityEvent event) throws JsonProcessingException {
        System.out.println("ViewEventManger.publish.eventId = " + event.getEventId());
        snsAsyncClient.publish(eventUtils.createPublishRequest(snsTopicArn, event))
                .thenAcceptAsync(publishResponse -> {
                    System.out.println("ViewEventManger.thenAcceptAsync.eventId = " + event.getEventId());
                    facilityEventLogRepository.publish(event.getEventId());
                });
    }

    @Override
    public List<FacilityEvent> getUnpublishedEvents() {
        return List.of();
    }

    @Override
    public void updateStatus(List<String> eventIds) {

    }
}
