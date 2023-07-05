package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.Industry;

import java.util.Optional;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, Long>{

    Optional<Industry> findByName(String name);

}
