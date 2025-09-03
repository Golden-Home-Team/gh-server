package kr.co.goldenhome.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.goldenhome.SNSEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import software.amazon.awssdk.services.sns.SnsAsyncClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@RequiredArgsConstructor
public abstract class EventManager<T extends SNSEvent> {

    protected final SnsAsyncClient snsAsyncClient;
    protected final EventUtils eventUtils;
    @Value("${aws.event.sns.endpoint}")
    protected String snsTopicArn;

    public abstract void saveLog(T event) throws JsonProcessingException;
    public abstract void publish(T event) throws JsonProcessingException;
    public abstract List<T> getUnpublishedEvents();
    public abstract void markAsPublished(List<String> eventIds);

    @Scheduled(fixedDelay = 60000)
    public void republish() {
        try {
            boolean hasEvents = true;
            while (hasEvents) {
                List<T> events = getUnpublishedEvents();
                if (events.isEmpty()) hasEvents = false;
                List<String> publishedEventId = republish(events).join();
                markAsPublished(publishedEventId);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private CompletableFuture<List<String>> republish(List<T> events) {
        CopyOnWriteArrayList<String> publishedEventIds = new CopyOnWriteArrayList<>();

        List<CompletableFuture<Void>> futures = events.stream()
                .map(event -> {
                    try {
                        return snsAsyncClient.publish(eventUtils.createPublishRequest(snsTopicArn, event))
                               .thenAcceptAsync(publishResponse -> publishedEventIds.add(event.getEventId()));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> publishedEventIds);
    }

}
