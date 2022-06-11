package com.markamber.testjhip.web.rest;

import static com.markamber.testjhip.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.markamber.testjhip.IntegrationTest;
import com.markamber.testjhip.domain.OrderLineItem;
import com.markamber.testjhip.repository.OrderLineItemRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrderLineItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderLineItemResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_EXPECTED_COST_UNIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXPECTED_COST_UNIT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SELL_PRICE_UNIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_SELL_PRICE_UNIT = new BigDecimal(2);

    private static final Long DEFAULT_NUMBER_UNITS = 1L;
    private static final Long UPDATED_NUMBER_UNITS = 2L;

    private static final String DEFAULT_ROOM = "AAAAAAAAAA";
    private static final String UPDATED_ROOM = "BBBBBBBBBB";

    private static final String DEFAULT_SYSTEM = "AAAAAAAAAA";
    private static final String UPDATED_SYSTEM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/order-line-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderLineItemMockMvc;

    private OrderLineItem orderLineItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderLineItem createEntity(EntityManager em) {
        OrderLineItem orderLineItem = new OrderLineItem()
            .description(DEFAULT_DESCRIPTION)
            .expectedCostUnit(DEFAULT_EXPECTED_COST_UNIT)
            .sellPriceUnit(DEFAULT_SELL_PRICE_UNIT)
            .numberUnits(DEFAULT_NUMBER_UNITS)
            .room(DEFAULT_ROOM)
            .system(DEFAULT_SYSTEM);
        return orderLineItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderLineItem createUpdatedEntity(EntityManager em) {
        OrderLineItem orderLineItem = new OrderLineItem()
            .description(UPDATED_DESCRIPTION)
            .expectedCostUnit(UPDATED_EXPECTED_COST_UNIT)
            .sellPriceUnit(UPDATED_SELL_PRICE_UNIT)
            .numberUnits(UPDATED_NUMBER_UNITS)
            .room(UPDATED_ROOM)
            .system(UPDATED_SYSTEM);
        return orderLineItem;
    }

    @BeforeEach
    public void initTest() {
        orderLineItem = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderLineItem() throws Exception {
        int databaseSizeBeforeCreate = orderLineItemRepository.findAll().size();
        // Create the OrderLineItem
        restOrderLineItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderLineItem)))
            .andExpect(status().isCreated());

        // Validate the OrderLineItem in the database
        List<OrderLineItem> orderLineItemList = orderLineItemRepository.findAll();
        assertThat(orderLineItemList).hasSize(databaseSizeBeforeCreate + 1);
        OrderLineItem testOrderLineItem = orderLineItemList.get(orderLineItemList.size() - 1);
        assertThat(testOrderLineItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrderLineItem.getExpectedCostUnit()).isEqualByComparingTo(DEFAULT_EXPECTED_COST_UNIT);
        assertThat(testOrderLineItem.getSellPriceUnit()).isEqualByComparingTo(DEFAULT_SELL_PRICE_UNIT);
        assertThat(testOrderLineItem.getNumberUnits()).isEqualTo(DEFAULT_NUMBER_UNITS);
        assertThat(testOrderLineItem.getRoom()).isEqualTo(DEFAULT_ROOM);
        assertThat(testOrderLineItem.getSystem()).isEqualTo(DEFAULT_SYSTEM);
    }

    @Test
    @Transactional
    void createOrderLineItemWithExistingId() throws Exception {
        // Create the OrderLineItem with an existing ID
        orderLineItem.setId(1L);

        int databaseSizeBeforeCreate = orderLineItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderLineItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderLineItem)))
            .andExpect(status().isBadRequest());

        // Validate the OrderLineItem in the database
        List<OrderLineItem> orderLineItemList = orderLineItemRepository.findAll();
        assertThat(orderLineItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderLineItems() throws Exception {
        // Initialize the database
        orderLineItemRepository.saveAndFlush(orderLineItem);

        // Get all the orderLineItemList
        restOrderLineItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderLineItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].expectedCostUnit").value(hasItem(sameNumber(DEFAULT_EXPECTED_COST_UNIT))))
            .andExpect(jsonPath("$.[*].sellPriceUnit").value(hasItem(sameNumber(DEFAULT_SELL_PRICE_UNIT))))
            .andExpect(jsonPath("$.[*].numberUnits").value(hasItem(DEFAULT_NUMBER_UNITS.intValue())))
            .andExpect(jsonPath("$.[*].room").value(hasItem(DEFAULT_ROOM)))
            .andExpect(jsonPath("$.[*].system").value(hasItem(DEFAULT_SYSTEM)));
    }

    @Test
    @Transactional
    void getOrderLineItem() throws Exception {
        // Initialize the database
        orderLineItemRepository.saveAndFlush(orderLineItem);

        // Get the orderLineItem
        restOrderLineItemMockMvc
            .perform(get(ENTITY_API_URL_ID, orderLineItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderLineItem.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.expectedCostUnit").value(sameNumber(DEFAULT_EXPECTED_COST_UNIT)))
            .andExpect(jsonPath("$.sellPriceUnit").value(sameNumber(DEFAULT_SELL_PRICE_UNIT)))
            .andExpect(jsonPath("$.numberUnits").value(DEFAULT_NUMBER_UNITS.intValue()))
            .andExpect(jsonPath("$.room").value(DEFAULT_ROOM))
            .andExpect(jsonPath("$.system").value(DEFAULT_SYSTEM));
    }

    @Test
    @Transactional
    void getNonExistingOrderLineItem() throws Exception {
        // Get the orderLineItem
        restOrderLineItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderLineItem() throws Exception {
        // Initialize the database
        orderLineItemRepository.saveAndFlush(orderLineItem);

        int databaseSizeBeforeUpdate = orderLineItemRepository.findAll().size();

        // Update the orderLineItem
        OrderLineItem updatedOrderLineItem = orderLineItemRepository.findById(orderLineItem.getId()).get();
        // Disconnect from session so that the updates on updatedOrderLineItem are not directly saved in db
        em.detach(updatedOrderLineItem);
        updatedOrderLineItem
            .description(UPDATED_DESCRIPTION)
            .expectedCostUnit(UPDATED_EXPECTED_COST_UNIT)
            .sellPriceUnit(UPDATED_SELL_PRICE_UNIT)
            .numberUnits(UPDATED_NUMBER_UNITS)
            .room(UPDATED_ROOM)
            .system(UPDATED_SYSTEM);

        restOrderLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderLineItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrderLineItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderLineItem in the database
        List<OrderLineItem> orderLineItemList = orderLineItemRepository.findAll();
        assertThat(orderLineItemList).hasSize(databaseSizeBeforeUpdate);
        OrderLineItem testOrderLineItem = orderLineItemList.get(orderLineItemList.size() - 1);
        assertThat(testOrderLineItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrderLineItem.getExpectedCostUnit()).isEqualByComparingTo(UPDATED_EXPECTED_COST_UNIT);
        assertThat(testOrderLineItem.getSellPriceUnit()).isEqualByComparingTo(UPDATED_SELL_PRICE_UNIT);
        assertThat(testOrderLineItem.getNumberUnits()).isEqualTo(UPDATED_NUMBER_UNITS);
        assertThat(testOrderLineItem.getRoom()).isEqualTo(UPDATED_ROOM);
        assertThat(testOrderLineItem.getSystem()).isEqualTo(UPDATED_SYSTEM);
    }

    @Test
    @Transactional
    void putNonExistingOrderLineItem() throws Exception {
        int databaseSizeBeforeUpdate = orderLineItemRepository.findAll().size();
        orderLineItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderLineItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderLineItem in the database
        List<OrderLineItem> orderLineItemList = orderLineItemRepository.findAll();
        assertThat(orderLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderLineItem() throws Exception {
        int databaseSizeBeforeUpdate = orderLineItemRepository.findAll().size();
        orderLineItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderLineItem in the database
        List<OrderLineItem> orderLineItemList = orderLineItemRepository.findAll();
        assertThat(orderLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderLineItem() throws Exception {
        int databaseSizeBeforeUpdate = orderLineItemRepository.findAll().size();
        orderLineItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderLineItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderLineItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderLineItem in the database
        List<OrderLineItem> orderLineItemList = orderLineItemRepository.findAll();
        assertThat(orderLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderLineItemWithPatch() throws Exception {
        // Initialize the database
        orderLineItemRepository.saveAndFlush(orderLineItem);

        int databaseSizeBeforeUpdate = orderLineItemRepository.findAll().size();

        // Update the orderLineItem using partial update
        OrderLineItem partialUpdatedOrderLineItem = new OrderLineItem();
        partialUpdatedOrderLineItem.setId(orderLineItem.getId());

        partialUpdatedOrderLineItem.sellPriceUnit(UPDATED_SELL_PRICE_UNIT).numberUnits(UPDATED_NUMBER_UNITS).room(UPDATED_ROOM);

        restOrderLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderLineItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderLineItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderLineItem in the database
        List<OrderLineItem> orderLineItemList = orderLineItemRepository.findAll();
        assertThat(orderLineItemList).hasSize(databaseSizeBeforeUpdate);
        OrderLineItem testOrderLineItem = orderLineItemList.get(orderLineItemList.size() - 1);
        assertThat(testOrderLineItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrderLineItem.getExpectedCostUnit()).isEqualByComparingTo(DEFAULT_EXPECTED_COST_UNIT);
        assertThat(testOrderLineItem.getSellPriceUnit()).isEqualByComparingTo(UPDATED_SELL_PRICE_UNIT);
        assertThat(testOrderLineItem.getNumberUnits()).isEqualTo(UPDATED_NUMBER_UNITS);
        assertThat(testOrderLineItem.getRoom()).isEqualTo(UPDATED_ROOM);
        assertThat(testOrderLineItem.getSystem()).isEqualTo(DEFAULT_SYSTEM);
    }

    @Test
    @Transactional
    void fullUpdateOrderLineItemWithPatch() throws Exception {
        // Initialize the database
        orderLineItemRepository.saveAndFlush(orderLineItem);

        int databaseSizeBeforeUpdate = orderLineItemRepository.findAll().size();

        // Update the orderLineItem using partial update
        OrderLineItem partialUpdatedOrderLineItem = new OrderLineItem();
        partialUpdatedOrderLineItem.setId(orderLineItem.getId());

        partialUpdatedOrderLineItem
            .description(UPDATED_DESCRIPTION)
            .expectedCostUnit(UPDATED_EXPECTED_COST_UNIT)
            .sellPriceUnit(UPDATED_SELL_PRICE_UNIT)
            .numberUnits(UPDATED_NUMBER_UNITS)
            .room(UPDATED_ROOM)
            .system(UPDATED_SYSTEM);

        restOrderLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderLineItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderLineItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderLineItem in the database
        List<OrderLineItem> orderLineItemList = orderLineItemRepository.findAll();
        assertThat(orderLineItemList).hasSize(databaseSizeBeforeUpdate);
        OrderLineItem testOrderLineItem = orderLineItemList.get(orderLineItemList.size() - 1);
        assertThat(testOrderLineItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrderLineItem.getExpectedCostUnit()).isEqualByComparingTo(UPDATED_EXPECTED_COST_UNIT);
        assertThat(testOrderLineItem.getSellPriceUnit()).isEqualByComparingTo(UPDATED_SELL_PRICE_UNIT);
        assertThat(testOrderLineItem.getNumberUnits()).isEqualTo(UPDATED_NUMBER_UNITS);
        assertThat(testOrderLineItem.getRoom()).isEqualTo(UPDATED_ROOM);
        assertThat(testOrderLineItem.getSystem()).isEqualTo(UPDATED_SYSTEM);
    }

    @Test
    @Transactional
    void patchNonExistingOrderLineItem() throws Exception {
        int databaseSizeBeforeUpdate = orderLineItemRepository.findAll().size();
        orderLineItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderLineItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderLineItem in the database
        List<OrderLineItem> orderLineItemList = orderLineItemRepository.findAll();
        assertThat(orderLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderLineItem() throws Exception {
        int databaseSizeBeforeUpdate = orderLineItemRepository.findAll().size();
        orderLineItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderLineItem in the database
        List<OrderLineItem> orderLineItemList = orderLineItemRepository.findAll();
        assertThat(orderLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderLineItem() throws Exception {
        int databaseSizeBeforeUpdate = orderLineItemRepository.findAll().size();
        orderLineItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderLineItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderLineItem in the database
        List<OrderLineItem> orderLineItemList = orderLineItemRepository.findAll();
        assertThat(orderLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderLineItem() throws Exception {
        // Initialize the database
        orderLineItemRepository.saveAndFlush(orderLineItem);

        int databaseSizeBeforeDelete = orderLineItemRepository.findAll().size();

        // Delete the orderLineItem
        restOrderLineItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderLineItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderLineItem> orderLineItemList = orderLineItemRepository.findAll();
        assertThat(orderLineItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
