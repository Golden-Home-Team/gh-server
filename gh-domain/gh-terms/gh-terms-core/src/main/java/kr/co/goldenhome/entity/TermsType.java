package kr.co.goldenhome.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TermsType {
    SERVICE_USAGE_TERMS("서비스 이용약관"),
    PRIVACY_POLICY("개인정보 처리방침"),
    LOCATION_SERVICE_TERMS("위치기반 서비스 이용약관");

    private final String value;
}
