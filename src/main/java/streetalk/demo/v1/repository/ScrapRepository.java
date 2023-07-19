package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.*;

import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<PostScrap,Long> {
    Optional<PostScrap> findByPostAndUser(Post post, User user);
}
