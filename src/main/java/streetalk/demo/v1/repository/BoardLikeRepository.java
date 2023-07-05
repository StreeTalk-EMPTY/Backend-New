package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import streetalk.demo.v1.domain.BoardLike;

public interface BoardLikeRepository extends JpaRepository<BoardLike,Long> {
}
