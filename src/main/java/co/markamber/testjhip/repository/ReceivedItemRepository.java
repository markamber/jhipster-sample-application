package co.markamber.testjhip.repository;

import co.markamber.testjhip.domain.ReceivedItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReceivedItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReceivedItemRepository extends JpaRepository<ReceivedItem, Long> {}
