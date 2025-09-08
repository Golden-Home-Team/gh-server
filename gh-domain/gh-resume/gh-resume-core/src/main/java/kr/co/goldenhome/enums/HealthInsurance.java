package kr.co.goldenhome.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HealthInsurance {
    NATIONAL("국민건강보험"), MEDICAL_AID_TYPE_1("의료급여1종"), MEDICAL_AID_TYPE_2("의료급여2종");
    private final String value;
}
