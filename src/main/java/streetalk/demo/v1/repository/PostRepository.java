package streetalk.demo.v1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findPostsByBoard(Board board);

    Optional<Post> findById(Long id);

    Post findFirstByOrderByCreatedDateDesc();

    List<Post>findByTitleContainingOrContentContaining(String title, String content);

    List<Post> findPostsByBoardAndIndustryAndLocation(Board board, Industry industry, Location location);

//    Page<Post> findByIdLessThanAndBoard(Long postId, Board board, Pageable pageable);

    List<Post> findByIdLessThanAndBoard(Long postId, Board board, Pageable pageable);

    List<Post> findByIdLessThanAndBoardAndIsDeletedIsFalse(Long postId, Board board, Pageable pageable);

    // 홈 - 인기글 - 내지역 => 최근 일주일 좋아요 순 5개
    List<Post> findTop5ByLocationAndIsDeletedFalseAndCreatedDateAfterOrderByLikeCountDesc(Location location, LocalDateTime localDateTime);

    // 메인 게시판 내지역 => 지역 전체
    List<Post> findByLocationAndIdLessThanAndIsDeletedFalseOrderByCreatedDateDesc(Location location, Long postId, Pageable pageable);


//    List<Post> findTop5ByIsDeletedIsFalseAndCreatedDateAfterAndIndustryOrderByLikeCountDesc(LocalDateTime localDateTime, Industry industry);

    // 홈 - 인기글 - 내업종 => 최근 일주일 좋아요 순 5개
    List<Post> findTop5ByIndustryAndIsDeletedFalseAndCreatedDateAfterOrderByLikeCountDesc(Industry industry, LocalDateTime localDateTime);

    // 메인 게시판 내업종 => 업종 전체
    @Query("SELECT p FROM Post p LEFT JOIN BlockedUser bu ON p.user.id = bu.blockedUserId WHERE p.industry = :industry AND p.id < :postId AND p.isDeleted = false AND (bu IS NULL OR bu.user.id <> :userId) ORDER BY p.createdDate DESC")
    List<Post> findByIndustryAndIdLessThanAndIsDeletedFalseOrderByCreatedDateDesc(Industry industry, Long postId, Long userId, Pageable pageable);
//    List<Post> findByIndustryAndIdLessThanAndIsDeletedFalseOrderByCreatedDateDesc(Industry industry, Long postId, Pageable pageable);

    // 홈 - 인기글 - 실시간 => 최근 일주일 좋아요 순 5개
    List<Post> findTop5ByIsDeletedFalseAndCreatedDateAfterOrderByLikeCountDesc(LocalDateTime localDateTime);

    // 메인 게시판 핫게시판 => 최근 일주일 좋아요 5개 이상 작성순
    List<Post> findByIdLessThanAndIsDeletedFalseAndLikeCountGreaterThanEqualAndCreatedDateAfterOrderByCreatedDateDesc(Long postId, Long LikeCountThreshold, LocalDateTime localDateTime, Pageable pageable);


    List<Post> findByUser(User user);
}
