package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.Board;
import streetalk.demo.v1.domain.Post;
import streetalk.demo.v1.domain.Reply;
import streetalk.demo.v1.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply,Long> {
    Optional<Reply>findByPost(Post post);

    List<Reply> findRepliesByUserId(Long userId);
}
