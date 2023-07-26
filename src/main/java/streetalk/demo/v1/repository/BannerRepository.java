package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner,Long> {


}
