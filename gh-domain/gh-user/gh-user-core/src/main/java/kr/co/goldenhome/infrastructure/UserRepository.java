package kr.co.goldenhome.infrastructure;

import kr.co.goldenhome.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    Optional<User> findByLoginId(String loginId);
}
