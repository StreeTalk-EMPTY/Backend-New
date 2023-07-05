package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface LockPostRepository extends JpaRepository<LockPost,Long> {
    Optional<LockPost>findByPostAndUser(Post post, User user);
    List<LockPost> findByPost(Post post);


}
