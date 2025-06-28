package kr.co.goldenhome.entity;

import jakarta.persistence.*;
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
    private String username;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private User(Long id, String username, String email, String password, UserRole userRole, UserStatus userStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.userStatus = userStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(String username, String email, String password, UserRole userRole) {
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .userRole(userRole)
                .userStatus(UserStatus.PENDING)
                .build();
    }

    public void active() {
        this.userStatus = UserStatus.ACTIVE;
    }
}
