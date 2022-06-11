package co.markamber.testjhip.repository;

import co.markamber.testjhip.domain.PurchaseOrderLineItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PurchaseOrderLineItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseOrderLineItemRepository extends JpaRepository<PurchaseOrderLineItem, Long> {}
