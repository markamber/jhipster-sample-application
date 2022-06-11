package co.markamber.testjhip.web.rest;

import co.markamber.testjhip.domain.OrderLineItem;
import co.markamber.testjhip.repository.OrderLineItemRepository;
import co.markamber.testjhip.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.markamber.testjhip.domain.OrderLineItem}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrderLineItemResource {

    private final Logger log = LoggerFactory.getLogger(OrderLineItemResource.class);

    private static final String ENTITY_NAME = "orderLineItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderLineItemRepository orderLineItemRepository;

    public OrderLineItemResource(OrderLineItemRepository orderLineItemRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
    }

    /**
     * {@code POST  /order-line-items} : Create a new orderLineItem.
     *
     * @param orderLineItem the orderLineItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderLineItem, or with status {@code 400 (Bad Request)} if the orderLineItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-line-items")
    public ResponseEntity<OrderLineItem> createOrderLineItem(@RequestBody OrderLineItem orderLineItem) throws URISyntaxException {
        log.debug("REST request to save OrderLineItem : {}", orderLineItem);
        if (orderLineItem.getId() != null) {
            throw new BadRequestAlertException("A new orderLineItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderLineItem result = orderLineItemRepository.save(orderLineItem);
        return ResponseEntity
            .created(new URI("/api/order-line-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-line-items/:id} : Updates an existing orderLineItem.
     *
     * @param id the id of the orderLineItem to save.
     * @param orderLineItem the orderLineItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderLineItem,
     * or with status {@code 400 (Bad Request)} if the orderLineItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderLineItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-line-items/{id}")
    public ResponseEntity<OrderLineItem> updateOrderLineItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderLineItem orderLineItem
    ) throws URISyntaxException {
        log.debug("REST request to update OrderLineItem : {}, {}", id, orderLineItem);
        if (orderLineItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderLineItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderLineItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderLineItem result = orderLineItemRepository.save(orderLineItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orderLineItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-line-items/:id} : Partial updates given fields of an existing orderLineItem, field will ignore if it is null
     *
     * @param id the id of the orderLineItem to save.
     * @param orderLineItem the orderLineItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderLineItem,
     * or with status {@code 400 (Bad Request)} if the orderLineItem is not valid,
     * or with status {@code 404 (Not Found)} if the orderLineItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderLineItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-line-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderLineItem> partialUpdateOrderLineItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderLineItem orderLineItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderLineItem partially : {}, {}", id, orderLineItem);
        if (orderLineItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderLineItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderLineItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderLineItem> result = orderLineItemRepository
            .findById(orderLineItem.getId())
            .map(existingOrderLineItem -> {
                if (orderLineItem.getDescription() != null) {
                    existingOrderLineItem.setDescription(orderLineItem.getDescription());
                }
                if (orderLineItem.getType() != null) {
                    existingOrderLineItem.setType(orderLineItem.getType());
                }
                if (orderLineItem.getExpectedCostUnit() != null) {
                    existingOrderLineItem.setExpectedCostUnit(orderLineItem.getExpectedCostUnit());
                }
                if (orderLineItem.getSellPriceUnit() != null) {
                    existingOrderLineItem.setSellPriceUnit(orderLineItem.getSellPriceUnit());
                }
                if (orderLineItem.getNumberUnits() != null) {
                    existingOrderLineItem.setNumberUnits(orderLineItem.getNumberUnits());
                }
                if (orderLineItem.getRoom() != null) {
                    existingOrderLineItem.setRoom(orderLineItem.getRoom());
                }
                if (orderLineItem.getSystem() != null) {
                    existingOrderLineItem.setSystem(orderLineItem.getSystem());
                }

                return existingOrderLineItem;
            })
            .map(orderLineItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orderLineItem.getId().toString())
        );
    }

    /**
     * {@code GET  /order-line-items} : get all the orderLineItems.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderLineItems in body.
     */
    @GetMapping("/order-line-items")
    public List<OrderLineItem> getAllOrderLineItems(@RequestParam(required = false) String filter) {
        if ("fufilledby-is-null".equals(filter)) {
            log.debug("REST request to get all OrderLineItems where fufilledBy is null");
            return StreamSupport
                .stream(orderLineItemRepository.findAll().spliterator(), false)
                .filter(orderLineItem -> orderLineItem.getFufilledBy() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all OrderLineItems");
        return orderLineItemRepository.findAll();
    }

    /**
     * {@code GET  /order-line-items/:id} : get the "id" orderLineItem.
     *
     * @param id the id of the orderLineItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderLineItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-line-items/{id}")
    public ResponseEntity<OrderLineItem> getOrderLineItem(@PathVariable Long id) {
        log.debug("REST request to get OrderLineItem : {}", id);
        Optional<OrderLineItem> orderLineItem = orderLineItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orderLineItem);
    }

    /**
     * {@code DELETE  /order-line-items/:id} : delete the "id" orderLineItem.
     *
     * @param id the id of the orderLineItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-line-items/{id}")
    public ResponseEntity<Void> deleteOrderLineItem(@PathVariable Long id) {
        log.debug("REST request to delete OrderLineItem : {}", id);
        orderLineItemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
