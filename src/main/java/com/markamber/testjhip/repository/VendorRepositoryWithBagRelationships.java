package com.markamber.testjhip.repository;

import com.markamber.testjhip.domain.Vendor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface VendorRepositoryWithBagRelationships {
    Optional<Vendor> fetchBagRelationships(Optional<Vendor> vendor);

    List<Vendor> fetchBagRelationships(List<Vendor> vendors);

    Page<Vendor> fetchBagRelationships(Page<Vendor> vendors);
}
