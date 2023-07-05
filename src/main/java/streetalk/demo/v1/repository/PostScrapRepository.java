package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.Post;
import streetalk.demo.v1.domain.PostScarp;
import streetalk.demo.v1.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostScrapRepository extends JpaRepository<PostScarp,Long> {
    Optional<PostScarp>findByPostAndUser(Post post, User user);
    List<PostScarp>findByUser(User user);

//    void delete(Optional<PostScarp> scrapLike);
}
