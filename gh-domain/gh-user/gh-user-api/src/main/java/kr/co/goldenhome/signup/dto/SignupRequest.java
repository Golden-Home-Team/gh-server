package kr.co.goldenhome.signup.dto;


public record SignupRequest(String loginId, String email,
                            String password, String phoneNumber)  {
}
