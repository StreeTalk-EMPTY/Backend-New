package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import streetalk.demo.v1.domain.Board;
import streetalk.demo.v1.domain.BoardLike;
import streetalk.demo.v1.domain.User;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike,Long> {

    Optional<BoardLike> findBoardLikeByUserAndBoard(User user, Board board);
    Optional<BoardLike> findBoardLikeByUserAndBoardId(User user, Long boardId);
}
