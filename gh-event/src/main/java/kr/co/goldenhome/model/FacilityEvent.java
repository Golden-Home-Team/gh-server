package kr.co.goldenhome.model;


import lombok.Getter;

@Getter
public class FacilityEvent extends SNSEvent {

    private Long facilityId;
    private FacilityEventType eventType;

    private FacilityEvent(String eventId, Long facilityId, FacilityEventType eventType) {
        super(eventId);
        this.facilityId = facilityId;
        this.eventType = eventType;
    }

    public static FacilityEvent create(Long facilityId, FacilityEventType eventType) {
        return new FacilityEvent(
                generateEventId(),
                facilityId,
                eventType
        );
    }

}
