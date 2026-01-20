package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Table(name = "recent_views")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class RecentView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long facilityId;

    @LastModifiedDate
    private LocalDateTime viewedAt;

    @Builder
    private RecentView(Long id, Long userId, Long facilityId, LocalDateTime viewedAt) {
        this.id = id;
        this.userId = userId;
        this.facilityId = facilityId;
        this.viewedAt = viewedAt;
    }

    public static RecentView create(Long userId, Long facilityId) {
        return RecentView.builder()
                .userId(userId)
                .facilityId(facilityId)
                .viewedAt(LocalDateTime.now())
                .build();
    }

    public void view() {
        this.viewedAt = LocalDateTime.now();
    }
}
