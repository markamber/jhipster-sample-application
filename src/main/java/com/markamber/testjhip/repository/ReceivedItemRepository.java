package com.markamber.testjhip.repository;

import com.markamber.testjhip.domain.ReceivedItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReceivedItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReceivedItemRepository extends JpaRepository<ReceivedItem, Long> {}
