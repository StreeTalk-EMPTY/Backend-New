package streetalk.demo.v1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplySaveRepository extends JpaRepository<ReplySave,Long> {
    Boolean existsByUserAndPost(User user,Post post);
    Optional<ReplySave> findAllByPostId(Long id);
    List<ReplySave> findAllByPost(Post post);
}