package com.markamber.testjhip.web.rest;

import com.markamber.testjhip.domain.ReceivedItem;
import com.markamber.testjhip.repository.ReceivedItemRepository;
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
 * REST controller for managing {@link com.markamber.testjhip.domain.ReceivedItem}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReceivedItemResource {

    private final Logger log = LoggerFactory.getLogger(ReceivedItemResource.class);

    private static final String ENTITY_NAME = "receivedItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReceivedItemRepository receivedItemRepository;

    public ReceivedItemResource(ReceivedItemRepository receivedItemRepository) {
        this.receivedItemRepository = receivedItemRepository;
    }

    /**
     * {@code POST  /received-items} : Create a new receivedItem.
     *
     * @param receivedItem the receivedItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new receivedItem, or with status {@code 400 (Bad Request)} if the receivedItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/received-items")
    public ResponseEntity<ReceivedItem> createReceivedItem(@RequestBody ReceivedItem receivedItem) throws URISyntaxException {
        log.debug("REST request to save ReceivedItem : {}", receivedItem);
        if (receivedItem.getId() != null) {
            throw new BadRequestAlertException("A new receivedItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReceivedItem result = receivedItemRepository.save(receivedItem);
        return ResponseEntity
            .created(new URI("/api/received-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /received-items/:id} : Updates an existing receivedItem.
     *
     * @param id the id of the receivedItem to save.
     * @param receivedItem the receivedItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receivedItem,
     * or with status {@code 400 (Bad Request)} if the receivedItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the receivedItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/received-items/{id}")
    public ResponseEntity<ReceivedItem> updateReceivedItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReceivedItem receivedItem
    ) throws URISyntaxException {
        log.debug("REST request to update ReceivedItem : {}, {}", id, receivedItem);
        if (receivedItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receivedItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!receivedItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReceivedItem result = receivedItemRepository.save(receivedItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, receivedItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /received-items/:id} : Partial updates given fields of an existing receivedItem, field will ignore if it is null
     *
     * @param id the id of the receivedItem to save.
     * @param receivedItem the receivedItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receivedItem,
     * or with status {@code 400 (Bad Request)} if the receivedItem is not valid,
     * or with status {@code 404 (Not Found)} if the receivedItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the receivedItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/received-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReceivedItem> partialUpdateReceivedItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReceivedItem receivedItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReceivedItem partially : {}, {}", id, receivedItem);
        if (receivedItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receivedItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!receivedItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReceivedItem> result = receivedItemRepository
            .findById(receivedItem.getId())
            .map(existingReceivedItem -> {
                if (receivedItem.getNote() != null) {
                    existingReceivedItem.setNote(receivedItem.getNote());
                }
                if (receivedItem.getLocation() != null) {
                    existingReceivedItem.setLocation(receivedItem.getLocation());
                }
                if (receivedItem.getAssetIdSerial() != null) {
                    existingReceivedItem.setAssetIdSerial(receivedItem.getAssetIdSerial());
                }
                if (receivedItem.getAssetIdMAC() != null) {
                    existingReceivedItem.setAssetIdMAC(receivedItem.getAssetIdMAC());
                }
                if (receivedItem.getReceivedDate() != null) {
                    existingReceivedItem.setReceivedDate(receivedItem.getReceivedDate());
                }
                if (receivedItem.getTracked() != null) {
                    existingReceivedItem.setTracked(receivedItem.getTracked());
                }
                if (receivedItem.getForInventory() != null) {
                    existingReceivedItem.setForInventory(receivedItem.getForInventory());
                }
                if (receivedItem.getBundleQuantity() != null) {
                    existingReceivedItem.setBundleQuantity(receivedItem.getBundleQuantity());
                }

                return existingReceivedItem;
            })
            .map(receivedItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, receivedItem.getId().toString())
        );
    }

    /**
     * {@code GET  /received-items} : get all the receivedItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of receivedItems in body.
     */
    @GetMapping("/received-items")
    public List<ReceivedItem> getAllReceivedItems() {
        log.debug("REST request to get all ReceivedItems");
        return receivedItemRepository.findAll();
    }

    /**
     * {@code GET  /received-items/:id} : get the "id" receivedItem.
     *
     * @param id the id of the receivedItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the receivedItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/received-items/{id}")
    public ResponseEntity<ReceivedItem> getReceivedItem(@PathVariable Long id) {
        log.debug("REST request to get ReceivedItem : {}", id);
        Optional<ReceivedItem> receivedItem = receivedItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(receivedItem);
    }

    /**
     * {@code DELETE  /received-items/:id} : delete the "id" receivedItem.
     *
     * @param id the id of the receivedItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/received-items/{id}")
    public ResponseEntity<Void> deleteReceivedItem(@PathVariable Long id) {
        log.debug("REST request to delete ReceivedItem : {}", id);
        receivedItemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
