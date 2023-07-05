package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import streetalk.demo.v1.domain.Location;
import streetalk.demo.v1.domain.User;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>{

    Optional<Location> findByFullName(String fullName);

}
