package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import streetalk.demo.v1.domain.BlockedUser;
import streetalk.demo.v1.domain.User;

import java.util.Optional;

public interface BlockedUserRepository extends JpaRepository<BlockedUser, Long> {

    Optional<BlockedUser> findByUserAndBlockedUserId(User currentUser, Long blockedUserId);
}