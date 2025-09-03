package kr.co.goldenhome.event;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "event_duplication_logs")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventDeduplicationLog {

    @Id
    private String id;
    private LocalDateTime createdAt;

    private EventDeduplicationLog(String id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public static EventDeduplicationLog create(String id) {
        return new EventDeduplicationLog(id, LocalDateTime.now());
    }
}
