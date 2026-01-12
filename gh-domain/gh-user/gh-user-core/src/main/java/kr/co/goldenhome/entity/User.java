package kr.co.goldenhome.entity;

import jakarta.persistence.*;
import kr.co.goldenhome.enums.ProviderType;
import kr.co.goldenhome.enums.UserRole;
import kr.co.goldenhome.enums.UserStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;


@SQLRestriction("user_status <> 'DELETED'")
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
    @Column(unique = true)
    private String email;
    private String password;
    private String phoneNumber;
    private String username;
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;
    @Column(unique = true)
    private String providerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private User(Long id, String loginId, String phoneNumber, String username, String email, String password, UserRole role, UserStatus status, ProviderType providerType, String providerId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.role = role;
        this.status = status;
        this.providerType = providerType;
        this.providerId = providerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(String loginId, String email, String password, String phoneNumber, UserRole role) {
        return User.builder()
                .loginId(loginId)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .role(role)
                .status(UserStatus.ACTIVE)
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
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void resetPassword(String password) {
        this.password = password;
    }

    public void resetEmail(String email) {
        this.email = email;
    }

    public void resetPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void modifyName(String name) {
        this.username = name;
    }

    public void modifyLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void withdraw() {
        this.status = UserStatus.DELETED;
    }


}
