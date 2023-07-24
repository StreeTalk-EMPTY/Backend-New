package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.PostImages;

public interface PostImagesRepository extends JpaRepository<PostImages, Long> {

    void deleteAllByPostId(Long postId);


}