package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface LockReplyRepository extends JpaRepository<LockReply,Long> {
    Optional<LockReply>findByReplyAndUser(Reply reply,User user);
}
