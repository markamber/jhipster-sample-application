package com.markamber.testjhip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.markamber.testjhip.IntegrationTest;
import com.markamber.testjhip.domain.PurchaseOrderLineItem;
import com.markamber.testjhip.repository.PurchaseOrderLineItemRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PurchaseOrderLineItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PurchaseOrderLineItemResourceIT {

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ESTIMATED_SHIP_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ESTIMATED_SHIP_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/purchase-order-line-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchaseOrderLineItemRepository purchaseOrderLineItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchaseOrderLineItemMockMvc;

    private PurchaseOrderLineItem purchaseOrderLineItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrderLineItem createEntity(EntityManager em) {
        PurchaseOrderLineItem purchaseOrderLineItem = new PurchaseOrderLineItem()
            .note(DEFAULT_NOTE)
            .estimatedShipDate(DEFAULT_ESTIMATED_SHIP_DATE);
        return purchaseOrderLineItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrderLineItem createUpdatedEntity(EntityManager em) {
        PurchaseOrderLineItem purchaseOrderLineItem = new PurchaseOrderLineItem()
            .note(UPDATED_NOTE)
            .estimatedShipDate(UPDATED_ESTIMATED_SHIP_DATE);
        return purchaseOrderLineItem;
    }

    @BeforeEach
    public void initTest() {
        purchaseOrderLineItem = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchaseOrderLineItem() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderLineItemRepository.findAll().size();
        // Create the PurchaseOrderLineItem
        restPurchaseOrderLineItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderLineItem))
            )
            .andExpect(status().isCreated());

        // Validate the PurchaseOrderLineItem in the database
        List<PurchaseOrderLineItem> purchaseOrderLineItemList = purchaseOrderLineItemRepository.findAll();
        assertThat(purchaseOrderLineItemList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseOrderLineItem testPurchaseOrderLineItem = purchaseOrderLineItemList.get(purchaseOrderLineItemList.size() - 1);
        assertThat(testPurchaseOrderLineItem.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testPurchaseOrderLineItem.getEstimatedShipDate()).isEqualTo(DEFAULT_ESTIMATED_SHIP_DATE);
    }

    @Test
    @Transactional
    void createPurchaseOrderLineItemWithExistingId() throws Exception {
        // Create the PurchaseOrderLineItem with an existing ID
        purchaseOrderLineItem.setId(1L);

        int databaseSizeBeforeCreate = purchaseOrderLineItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseOrderLineItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderLineItem in the database
        List<PurchaseOrderLineItem> purchaseOrderLineItemList = purchaseOrderLineItemRepository.findAll();
        assertThat(purchaseOrderLineItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderLineItems() throws Exception {
        // Initialize the database
        purchaseOrderLineItemRepository.saveAndFlush(purchaseOrderLineItem);

        // Get all the purchaseOrderLineItemList
        restPurchaseOrderLineItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrderLineItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].estimatedShipDate").value(hasItem(DEFAULT_ESTIMATED_SHIP_DATE.toString())));
    }

    @Test
    @Transactional
    void getPurchaseOrderLineItem() throws Exception {
        // Initialize the database
        purchaseOrderLineItemRepository.saveAndFlush(purchaseOrderLineItem);

        // Get the purchaseOrderLineItem
        restPurchaseOrderLineItemMockMvc
            .perform(get(ENTITY_API_URL_ID, purchaseOrderLineItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseOrderLineItem.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.estimatedShipDate").value(DEFAULT_ESTIMATED_SHIP_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPurchaseOrderLineItem() throws Exception {
        // Get the purchaseOrderLineItem
        restPurchaseOrderLineItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPurchaseOrderLineItem() throws Exception {
        // Initialize the database
        purchaseOrderLineItemRepository.saveAndFlush(purchaseOrderLineItem);

        int databaseSizeBeforeUpdate = purchaseOrderLineItemRepository.findAll().size();

        // Update the purchaseOrderLineItem
        PurchaseOrderLineItem updatedPurchaseOrderLineItem = purchaseOrderLineItemRepository.findById(purchaseOrderLineItem.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseOrderLineItem are not directly saved in db
        em.detach(updatedPurchaseOrderLineItem);
        updatedPurchaseOrderLineItem.note(UPDATED_NOTE).estimatedShipDate(UPDATED_ESTIMATED_SHIP_DATE);

        restPurchaseOrderLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPurchaseOrderLineItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPurchaseOrderLineItem))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderLineItem in the database
        List<PurchaseOrderLineItem> purchaseOrderLineItemList = purchaseOrderLineItemRepository.findAll();
        assertThat(purchaseOrderLineItemList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderLineItem testPurchaseOrderLineItem = purchaseOrderLineItemList.get(purchaseOrderLineItemList.size() - 1);
        assertThat(testPurchaseOrderLineItem.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testPurchaseOrderLineItem.getEstimatedShipDate()).isEqualTo(UPDATED_ESTIMATED_SHIP_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPurchaseOrderLineItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderLineItemRepository.findAll().size();
        purchaseOrderLineItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseOrderLineItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderLineItem in the database
        List<PurchaseOrderLineItem> purchaseOrderLineItemList = purchaseOrderLineItemRepository.findAll();
        assertThat(purchaseOrderLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchaseOrderLineItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderLineItemRepository.findAll().size();
        purchaseOrderLineItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderLineItem in the database
        List<PurchaseOrderLineItem> purchaseOrderLineItemList = purchaseOrderLineItemRepository.findAll();
        assertThat(purchaseOrderLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchaseOrderLineItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderLineItemRepository.findAll().size();
        purchaseOrderLineItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderLineItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderLineItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrderLineItem in the database
        List<PurchaseOrderLineItem> purchaseOrderLineItemList = purchaseOrderLineItemRepository.findAll();
        assertThat(purchaseOrderLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchaseOrderLineItemWithPatch() throws Exception {
        // Initialize the database
        purchaseOrderLineItemRepository.saveAndFlush(purchaseOrderLineItem);

        int databaseSizeBeforeUpdate = purchaseOrderLineItemRepository.findAll().size();

        // Update the purchaseOrderLineItem using partial update
        PurchaseOrderLineItem partialUpdatedPurchaseOrderLineItem = new PurchaseOrderLineItem();
        partialUpdatedPurchaseOrderLineItem.setId(purchaseOrderLineItem.getId());

        partialUpdatedPurchaseOrderLineItem.estimatedShipDate(UPDATED_ESTIMATED_SHIP_DATE);

        restPurchaseOrderLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrderLineItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseOrderLineItem))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderLineItem in the database
        List<PurchaseOrderLineItem> purchaseOrderLineItemList = purchaseOrderLineItemRepository.findAll();
        assertThat(purchaseOrderLineItemList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderLineItem testPurchaseOrderLineItem = purchaseOrderLineItemList.get(purchaseOrderLineItemList.size() - 1);
        assertThat(testPurchaseOrderLineItem.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testPurchaseOrderLineItem.getEstimatedShipDate()).isEqualTo(UPDATED_ESTIMATED_SHIP_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePurchaseOrderLineItemWithPatch() throws Exception {
        // Initialize the database
        purchaseOrderLineItemRepository.saveAndFlush(purchaseOrderLineItem);

        int databaseSizeBeforeUpdate = purchaseOrderLineItemRepository.findAll().size();

        // Update the purchaseOrderLineItem using partial update
        PurchaseOrderLineItem partialUpdatedPurchaseOrderLineItem = new PurchaseOrderLineItem();
        partialUpdatedPurchaseOrderLineItem.setId(purchaseOrderLineItem.getId());

        partialUpdatedPurchaseOrderLineItem.note(UPDATED_NOTE).estimatedShipDate(UPDATED_ESTIMATED_SHIP_DATE);

        restPurchaseOrderLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrderLineItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseOrderLineItem))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderLineItem in the database
        List<PurchaseOrderLineItem> purchaseOrderLineItemList = purchaseOrderLineItemRepository.findAll();
        assertThat(purchaseOrderLineItemList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderLineItem testPurchaseOrderLineItem = purchaseOrderLineItemList.get(purchaseOrderLineItemList.size() - 1);
        assertThat(testPurchaseOrderLineItem.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testPurchaseOrderLineItem.getEstimatedShipDate()).isEqualTo(UPDATED_ESTIMATED_SHIP_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPurchaseOrderLineItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderLineItemRepository.findAll().size();
        purchaseOrderLineItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchaseOrderLineItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderLineItem in the database
        List<PurchaseOrderLineItem> purchaseOrderLineItemList = purchaseOrderLineItemRepository.findAll();
        assertThat(purchaseOrderLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchaseOrderLineItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderLineItemRepository.findAll().size();
        purchaseOrderLineItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderLineItem in the database
        List<PurchaseOrderLineItem> purchaseOrderLineItemList = purchaseOrderLineItemRepository.findAll();
        assertThat(purchaseOrderLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchaseOrderLineItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderLineItemRepository.findAll().size();
        purchaseOrderLineItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderLineItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrderLineItem in the database
        List<PurchaseOrderLineItem> purchaseOrderLineItemList = purchaseOrderLineItemRepository.findAll();
        assertThat(purchaseOrderLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchaseOrderLineItem() throws Exception {
        // Initialize the database
        purchaseOrderLineItemRepository.saveAndFlush(purchaseOrderLineItem);

        int databaseSizeBeforeDelete = purchaseOrderLineItemRepository.findAll().size();

        // Delete the purchaseOrderLineItem
        restPurchaseOrderLineItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchaseOrderLineItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseOrderLineItem> purchaseOrderLineItemList = purchaseOrderLineItemRepository.findAll();
        assertThat(purchaseOrderLineItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
