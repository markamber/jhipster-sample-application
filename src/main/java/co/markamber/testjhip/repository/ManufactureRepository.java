package co.markamber.testjhip.repository;

import co.markamber.testjhip.domain.Manufacture;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Manufacture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManufactureRepository extends JpaRepository<Manufacture, Long> {}
