package kr.co.goldenhome.infrastructure;

import kr.co.goldenhome.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
