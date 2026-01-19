package kr.co.goldenhome.model;


import lombok.Getter;

@Getter
public class FacilityEvent extends SNSEvent {

    private Long facilityId;
    private Float avgScore;
    private FacilityEventType eventType;

    private FacilityEvent(String eventId,Long facilityId, Float avgScore, FacilityEventType eventType) {
        super(eventId);
        this.facilityId = facilityId;
        this.avgScore = avgScore;
        this.eventType = eventType;
    }

    public static FacilityEvent createReviewEvent(Long facilityId, FacilityEventType eventType, Float avgScore) {
        return new FacilityEvent(
                generateEventId(),
                facilityId,
                avgScore,
                eventType);
    }

    public static FacilityEvent createViewEvent(Long facilityId, FacilityEventType eventType) {
        return new FacilityEvent(generateEventId(), facilityId, null, eventType);
    }

}
