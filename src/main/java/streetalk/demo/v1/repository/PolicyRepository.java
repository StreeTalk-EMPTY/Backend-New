package streetalk.demo.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import streetalk.demo.v1.domain.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
}