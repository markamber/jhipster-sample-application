package co.markamber.testjhip.repository;

import co.markamber.testjhip.domain.OrderLineItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrderLineItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {}
