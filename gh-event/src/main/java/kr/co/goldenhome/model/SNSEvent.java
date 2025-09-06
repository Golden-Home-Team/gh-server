package kr.co.goldenhome.model;

import io.hypersistence.tsid.TSID;
import lombok.Getter;

@Getter
public abstract class SNSEvent {

    protected String eventId;

    protected SNSEvent(String eventId) {
        this.eventId = eventId;
    }

    public static String generateEventId() {
        return TSID.Factory.getTsid().toString();
    }
}
