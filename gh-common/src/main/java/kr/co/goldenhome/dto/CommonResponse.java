package kr.co.goldenhome.dto;

public record CommonResponse(boolean success) {

    public static CommonResponse ok() {
        return new CommonResponse(true);
    }
}
