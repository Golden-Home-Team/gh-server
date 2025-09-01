package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "facility_event_logs")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacilityEventLog {

    @Id
    private String eventId;
    private boolean isPublished;
    private String payload;
    private LocalDateTime createdAt;

    @Builder
    private FacilityEventLog(String eventId, boolean isPublished, String payload, LocalDateTime createdAt) {
        this.eventId = eventId;
        this.isPublished = isPublished;
        this.payload = payload;
        this.createdAt = createdAt;
    }

    public static FacilityEventLog create(String eventId, String payload) {
        return FacilityEventLog.builder()
                .eventId(eventId)
                .isPublished(false)
                .payload(payload)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
