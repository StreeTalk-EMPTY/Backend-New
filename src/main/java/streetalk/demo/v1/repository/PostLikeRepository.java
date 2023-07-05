package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.Post;
import streetalk.demo.v1.domain.PostLike;
import streetalk.demo.v1.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    Optional<PostLike>findByPostAndUser(Post post, User user);
    List<PostLike>findByUser(User user);

}
