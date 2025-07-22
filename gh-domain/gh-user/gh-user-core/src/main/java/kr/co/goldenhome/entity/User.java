package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.ProviderType;
import kr.co.goldenhome.enums.UserRole;
import kr.co.goldenhome.enums.UserStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String loginId;
    private String email;
    private String password;
    private String phoneNumber;
    private String username;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;
    @Column(unique = true)
    private String providerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private User(Long id, String loginId, String phoneNumber, String username, String email, String password, UserRole userRole, UserStatus userStatus, ProviderType providerType, String providerId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.userRole = userRole;
        this.userStatus = userStatus;
        this.providerType = providerType;
        this.providerId = providerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(String loginId, String email, String password, String phoneNumber, UserRole userRole) {
        return User.builder()
                .loginId(loginId)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .userRole(userRole)
                .userStatus(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static User socialLogin(ProviderType providerType, String providerId, String username) {
        return User.builder()
                .providerType(providerType)
                .providerId(providerId)
                .loginId(username)
                .username(username)
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}
