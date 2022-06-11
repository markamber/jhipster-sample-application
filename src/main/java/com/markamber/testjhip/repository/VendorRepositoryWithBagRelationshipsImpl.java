package com.markamber.testjhip.repository;

import com.markamber.testjhip.domain.Vendor;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class VendorRepositoryWithBagRelationshipsImpl implements VendorRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Vendor> fetchBagRelationships(Optional<Vendor> vendor) {
        return vendor.map(this::fetchManufactures);
    }

    @Override
    public Page<Vendor> fetchBagRelationships(Page<Vendor> vendors) {
        return new PageImpl<>(fetchBagRelationships(vendors.getContent()), vendors.getPageable(), vendors.getTotalElements());
    }

    @Override
    public List<Vendor> fetchBagRelationships(List<Vendor> vendors) {
        return Optional.of(vendors).map(this::fetchManufactures).orElse(Collections.emptyList());
    }

    Vendor fetchManufactures(Vendor result) {
        return entityManager
            .createQuery("select vendor from Vendor vendor left join fetch vendor.manufactures where vendor is :vendor", Vendor.class)
            .setParameter("vendor", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Vendor> fetchManufactures(List<Vendor> vendors) {
        return entityManager
            .createQuery(
                "select distinct vendor from Vendor vendor left join fetch vendor.manufactures where vendor in :vendors",
                Vendor.class
            )
            .setParameter("vendors", vendors)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
