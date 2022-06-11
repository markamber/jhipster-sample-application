package com.markamber.testjhip.web.rest;

import com.markamber.testjhip.domain.PurchaseOrderLineItem;
import com.markamber.testjhip.repository.PurchaseOrderLineItemRepository;
import com.markamber.testjhip.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.markamber.testjhip.domain.PurchaseOrderLineItem}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PurchaseOrderLineItemResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderLineItemResource.class);

    private static final String ENTITY_NAME = "purchaseOrderLineItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseOrderLineItemRepository purchaseOrderLineItemRepository;

    public PurchaseOrderLineItemResource(PurchaseOrderLineItemRepository purchaseOrderLineItemRepository) {
        this.purchaseOrderLineItemRepository = purchaseOrderLineItemRepository;
    }

    /**
     * {@code POST  /purchase-order-line-items} : Create a new purchaseOrderLineItem.
     *
     * @param purchaseOrderLineItem the purchaseOrderLineItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseOrderLineItem, or with status {@code 400 (Bad Request)} if the purchaseOrderLineItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchase-order-line-items")
    public ResponseEntity<PurchaseOrderLineItem> createPurchaseOrderLineItem(@RequestBody PurchaseOrderLineItem purchaseOrderLineItem)
        throws URISyntaxException {
        log.debug("REST request to save PurchaseOrderLineItem : {}", purchaseOrderLineItem);
        if (purchaseOrderLineItem.getId() != null) {
            throw new BadRequestAlertException("A new purchaseOrderLineItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseOrderLineItem result = purchaseOrderLineItemRepository.save(purchaseOrderLineItem);
        return ResponseEntity
            .created(new URI("/api/purchase-order-line-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchase-order-line-items/:id} : Updates an existing purchaseOrderLineItem.
     *
     * @param id the id of the purchaseOrderLineItem to save.
     * @param purchaseOrderLineItem the purchaseOrderLineItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrderLineItem,
     * or with status {@code 400 (Bad Request)} if the purchaseOrderLineItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrderLineItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchase-order-line-items/{id}")
    public ResponseEntity<PurchaseOrderLineItem> updatePurchaseOrderLineItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PurchaseOrderLineItem purchaseOrderLineItem
    ) throws URISyntaxException {
        log.debug("REST request to update PurchaseOrderLineItem : {}, {}", id, purchaseOrderLineItem);
        if (purchaseOrderLineItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseOrderLineItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseOrderLineItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PurchaseOrderLineItem result = purchaseOrderLineItemRepository.save(purchaseOrderLineItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseOrderLineItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /purchase-order-line-items/:id} : Partial updates given fields of an existing purchaseOrderLineItem, field will ignore if it is null
     *
     * @param id the id of the purchaseOrderLineItem to save.
     * @param purchaseOrderLineItem the purchaseOrderLineItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrderLineItem,
     * or with status {@code 400 (Bad Request)} if the purchaseOrderLineItem is not valid,
     * or with status {@code 404 (Not Found)} if the purchaseOrderLineItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrderLineItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/purchase-order-line-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchaseOrderLineItem> partialUpdatePurchaseOrderLineItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PurchaseOrderLineItem purchaseOrderLineItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update PurchaseOrderLineItem partially : {}, {}", id, purchaseOrderLineItem);
        if (purchaseOrderLineItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseOrderLineItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseOrderLineItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchaseOrderLineItem> result = purchaseOrderLineItemRepository
            .findById(purchaseOrderLineItem.getId())
            .map(existingPurchaseOrderLineItem -> {
                if (purchaseOrderLineItem.getNote() != null) {
                    existingPurchaseOrderLineItem.setNote(purchaseOrderLineItem.getNote());
                }
                if (purchaseOrderLineItem.getEstimatedShipDate() != null) {
                    existingPurchaseOrderLineItem.setEstimatedShipDate(purchaseOrderLineItem.getEstimatedShipDate());
                }

                return existingPurchaseOrderLineItem;
            })
            .map(purchaseOrderLineItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseOrderLineItem.getId().toString())
        );
    }

    /**
     * {@code GET  /purchase-order-line-items} : get all the purchaseOrderLineItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseOrderLineItems in body.
     */
    @GetMapping("/purchase-order-line-items")
    public List<PurchaseOrderLineItem> getAllPurchaseOrderLineItems() {
        log.debug("REST request to get all PurchaseOrderLineItems");
        return purchaseOrderLineItemRepository.findAll();
    }

    /**
     * {@code GET  /purchase-order-line-items/:id} : get the "id" purchaseOrderLineItem.
     *
     * @param id the id of the purchaseOrderLineItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseOrderLineItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchase-order-line-items/{id}")
    public ResponseEntity<PurchaseOrderLineItem> getPurchaseOrderLineItem(@PathVariable Long id) {
        log.debug("REST request to get PurchaseOrderLineItem : {}", id);
        Optional<PurchaseOrderLineItem> purchaseOrderLineItem = purchaseOrderLineItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(purchaseOrderLineItem);
    }

    /**
     * {@code DELETE  /purchase-order-line-items/:id} : delete the "id" purchaseOrderLineItem.
     *
     * @param id the id of the purchaseOrderLineItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchase-order-line-items/{id}")
    public ResponseEntity<Void> deletePurchaseOrderLineItem(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseOrderLineItem : {}", id);
        purchaseOrderLineItemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
