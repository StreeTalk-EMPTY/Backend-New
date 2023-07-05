package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.Board;

import java.util.List;
import java.util.Optional;
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByBoardNameContaining(String boardName);
    Optional<Board> findBoardByBoardName(String boardName);
    Optional<Board> findBoardById(Long id);
    List<Board> findBoardByCategory(String category);
}

