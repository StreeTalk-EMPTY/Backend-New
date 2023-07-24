package streetalk.demo.v1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findPostsByBoard(Board board);

    Optional<Post> findById(Long id);

    Post findFirstByOrderByCreatedDateDesc();

    List<Post>findByTitleContaining(String word);

    List<Post> findPostsByBoardAndIndustryAndLocation(Board board, Industry industry, Location location);

//    Page<Post> findByIdLessThanAndBoard(Long postId, Board board, Pageable pageable);

    List<Post> findByIdLessThanAndBoard(Long postId, Board board, Pageable pageable);

    List<Post> findByIdLessThanAndBoardAndIsDeletedIsFalse(Long postId, Board board, Pageable pageable);

    List<Post> findByCreatedDateAfterAndIndustry(LocalDateTime localDateTime, Industry industry);
    List<Post> findByCreatedDateAfterAndLocation(LocalDateTime localDateTime, Location location);
    List<Post> findTop5AndIsDeletedIsFalseByOrderByCreatedDateDesc();

    List<Post> findByUser(User user);
}
