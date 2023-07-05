package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.Notice;
import streetalk.demo.v1.domain.NoticeImgUrl;
import streetalk.demo.v1.domain.Post;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,Long> {
    Notice findFirstByOrderByCreatedDateAsc();
}
