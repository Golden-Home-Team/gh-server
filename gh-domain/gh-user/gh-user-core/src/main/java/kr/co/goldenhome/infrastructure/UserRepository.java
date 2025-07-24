package kr.co.goldenhome.infrastructure;

import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.enums.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByProviderTypeAndProviderId(ProviderType providerType, String providerId);
    Optional<User> findByEmail(String email);
}
