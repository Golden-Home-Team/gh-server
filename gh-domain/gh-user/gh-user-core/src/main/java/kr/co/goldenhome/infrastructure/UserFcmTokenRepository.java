package kr.co.goldenhome.infrastructure;

import kr.co.goldenhome.entity.UserFcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFcmTokenRepository extends JpaRepository<UserFcmToken, Long> {
    Optional<UserFcmToken> findByUserIdOrToken(Long userId, String token);
}
