package com.markamber.testjhip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.markamber.testjhip.IntegrationTest;
import com.markamber.testjhip.domain.ReceivedItem;
import com.markamber.testjhip.repository.ReceivedItemRepository;
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
 * Integration tests for the {@link ReceivedItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReceivedItemResourceIT {

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_ID_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_ID_SERIAL = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_ID_MAC = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_ID_MAC = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RECEIVED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECEIVED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_TRACKED = false;
    private static final Boolean UPDATED_TRACKED = true;

    private static final Boolean DEFAULT_FOR_INVENTORY = false;
    private static final Boolean UPDATED_FOR_INVENTORY = true;

    private static final Long DEFAULT_BUNDLE_QUANTITY = 1L;
    private static final Long UPDATED_BUNDLE_QUANTITY = 2L;

    private static final String ENTITY_API_URL = "/api/received-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReceivedItemRepository receivedItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReceivedItemMockMvc;

    private ReceivedItem receivedItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReceivedItem createEntity(EntityManager em) {
        ReceivedItem receivedItem = new ReceivedItem()
            .note(DEFAULT_NOTE)
            .location(DEFAULT_LOCATION)
            .assetIdSerial(DEFAULT_ASSET_ID_SERIAL)
            .assetIdMAC(DEFAULT_ASSET_ID_MAC)
            .receivedDate(DEFAULT_RECEIVED_DATE)
            .tracked(DEFAULT_TRACKED)
            .forInventory(DEFAULT_FOR_INVENTORY)
            .bundleQuantity(DEFAULT_BUNDLE_QUANTITY);
        return receivedItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReceivedItem createUpdatedEntity(EntityManager em) {
        ReceivedItem receivedItem = new ReceivedItem()
            .note(UPDATED_NOTE)
            .location(UPDATED_LOCATION)
            .assetIdSerial(UPDATED_ASSET_ID_SERIAL)
            .assetIdMAC(UPDATED_ASSET_ID_MAC)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .tracked(UPDATED_TRACKED)
            .forInventory(UPDATED_FOR_INVENTORY)
            .bundleQuantity(UPDATED_BUNDLE_QUANTITY);
        return receivedItem;
    }

    @BeforeEach
    public void initTest() {
        receivedItem = createEntity(em);
    }

    @Test
    @Transactional
    void createReceivedItem() throws Exception {
        int databaseSizeBeforeCreate = receivedItemRepository.findAll().size();
        // Create the ReceivedItem
        restReceivedItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receivedItem)))
            .andExpect(status().isCreated());

        // Validate the ReceivedItem in the database
        List<ReceivedItem> receivedItemList = receivedItemRepository.findAll();
        assertThat(receivedItemList).hasSize(databaseSizeBeforeCreate + 1);
        ReceivedItem testReceivedItem = receivedItemList.get(receivedItemList.size() - 1);
        assertThat(testReceivedItem.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testReceivedItem.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testReceivedItem.getAssetIdSerial()).isEqualTo(DEFAULT_ASSET_ID_SERIAL);
        assertThat(testReceivedItem.getAssetIdMAC()).isEqualTo(DEFAULT_ASSET_ID_MAC);
        assertThat(testReceivedItem.getReceivedDate()).isEqualTo(DEFAULT_RECEIVED_DATE);
        assertThat(testReceivedItem.getTracked()).isEqualTo(DEFAULT_TRACKED);
        assertThat(testReceivedItem.getForInventory()).isEqualTo(DEFAULT_FOR_INVENTORY);
        assertThat(testReceivedItem.getBundleQuantity()).isEqualTo(DEFAULT_BUNDLE_QUANTITY);
    }

    @Test
    @Transactional
    void createReceivedItemWithExistingId() throws Exception {
        // Create the ReceivedItem with an existing ID
        receivedItem.setId(1L);

        int databaseSizeBeforeCreate = receivedItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReceivedItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receivedItem)))
            .andExpect(status().isBadRequest());

        // Validate the ReceivedItem in the database
        List<ReceivedItem> receivedItemList = receivedItemRepository.findAll();
        assertThat(receivedItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReceivedItems() throws Exception {
        // Initialize the database
        receivedItemRepository.saveAndFlush(receivedItem);

        // Get all the receivedItemList
        restReceivedItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receivedItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].assetIdSerial").value(hasItem(DEFAULT_ASSET_ID_SERIAL)))
            .andExpect(jsonPath("$.[*].assetIdMAC").value(hasItem(DEFAULT_ASSET_ID_MAC)))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].tracked").value(hasItem(DEFAULT_TRACKED.booleanValue())))
            .andExpect(jsonPath("$.[*].forInventory").value(hasItem(DEFAULT_FOR_INVENTORY.booleanValue())))
            .andExpect(jsonPath("$.[*].bundleQuantity").value(hasItem(DEFAULT_BUNDLE_QUANTITY.intValue())));
    }

    @Test
    @Transactional
    void getReceivedItem() throws Exception {
        // Initialize the database
        receivedItemRepository.saveAndFlush(receivedItem);

        // Get the receivedItem
        restReceivedItemMockMvc
            .perform(get(ENTITY_API_URL_ID, receivedItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(receivedItem.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.assetIdSerial").value(DEFAULT_ASSET_ID_SERIAL))
            .andExpect(jsonPath("$.assetIdMAC").value(DEFAULT_ASSET_ID_MAC))
            .andExpect(jsonPath("$.receivedDate").value(DEFAULT_RECEIVED_DATE.toString()))
            .andExpect(jsonPath("$.tracked").value(DEFAULT_TRACKED.booleanValue()))
            .andExpect(jsonPath("$.forInventory").value(DEFAULT_FOR_INVENTORY.booleanValue()))
            .andExpect(jsonPath("$.bundleQuantity").value(DEFAULT_BUNDLE_QUANTITY.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingReceivedItem() throws Exception {
        // Get the receivedItem
        restReceivedItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReceivedItem() throws Exception {
        // Initialize the database
        receivedItemRepository.saveAndFlush(receivedItem);

        int databaseSizeBeforeUpdate = receivedItemRepository.findAll().size();

        // Update the receivedItem
        ReceivedItem updatedReceivedItem = receivedItemRepository.findById(receivedItem.getId()).get();
        // Disconnect from session so that the updates on updatedReceivedItem are not directly saved in db
        em.detach(updatedReceivedItem);
        updatedReceivedItem
            .note(UPDATED_NOTE)
            .location(UPDATED_LOCATION)
            .assetIdSerial(UPDATED_ASSET_ID_SERIAL)
            .assetIdMAC(UPDATED_ASSET_ID_MAC)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .tracked(UPDATED_TRACKED)
            .forInventory(UPDATED_FOR_INVENTORY)
            .bundleQuantity(UPDATED_BUNDLE_QUANTITY);

        restReceivedItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReceivedItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReceivedItem))
            )
            .andExpect(status().isOk());

        // Validate the ReceivedItem in the database
        List<ReceivedItem> receivedItemList = receivedItemRepository.findAll();
        assertThat(receivedItemList).hasSize(databaseSizeBeforeUpdate);
        ReceivedItem testReceivedItem = receivedItemList.get(receivedItemList.size() - 1);
        assertThat(testReceivedItem.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testReceivedItem.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testReceivedItem.getAssetIdSerial()).isEqualTo(UPDATED_ASSET_ID_SERIAL);
        assertThat(testReceivedItem.getAssetIdMAC()).isEqualTo(UPDATED_ASSET_ID_MAC);
        assertThat(testReceivedItem.getReceivedDate()).isEqualTo(UPDATED_RECEIVED_DATE);
        assertThat(testReceivedItem.getTracked()).isEqualTo(UPDATED_TRACKED);
        assertThat(testReceivedItem.getForInventory()).isEqualTo(UPDATED_FOR_INVENTORY);
        assertThat(testReceivedItem.getBundleQuantity()).isEqualTo(UPDATED_BUNDLE_QUANTITY);
    }

    @Test
    @Transactional
    void putNonExistingReceivedItem() throws Exception {
        int databaseSizeBeforeUpdate = receivedItemRepository.findAll().size();
        receivedItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceivedItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, receivedItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receivedItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceivedItem in the database
        List<ReceivedItem> receivedItemList = receivedItemRepository.findAll();
        assertThat(receivedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReceivedItem() throws Exception {
        int databaseSizeBeforeUpdate = receivedItemRepository.findAll().size();
        receivedItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceivedItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receivedItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceivedItem in the database
        List<ReceivedItem> receivedItemList = receivedItemRepository.findAll();
        assertThat(receivedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReceivedItem() throws Exception {
        int databaseSizeBeforeUpdate = receivedItemRepository.findAll().size();
        receivedItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceivedItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receivedItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReceivedItem in the database
        List<ReceivedItem> receivedItemList = receivedItemRepository.findAll();
        assertThat(receivedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReceivedItemWithPatch() throws Exception {
        // Initialize the database
        receivedItemRepository.saveAndFlush(receivedItem);

        int databaseSizeBeforeUpdate = receivedItemRepository.findAll().size();

        // Update the receivedItem using partial update
        ReceivedItem partialUpdatedReceivedItem = new ReceivedItem();
        partialUpdatedReceivedItem.setId(receivedItem.getId());

        partialUpdatedReceivedItem.location(UPDATED_LOCATION).bundleQuantity(UPDATED_BUNDLE_QUANTITY);

        restReceivedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceivedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceivedItem))
            )
            .andExpect(status().isOk());

        // Validate the ReceivedItem in the database
        List<ReceivedItem> receivedItemList = receivedItemRepository.findAll();
        assertThat(receivedItemList).hasSize(databaseSizeBeforeUpdate);
        ReceivedItem testReceivedItem = receivedItemList.get(receivedItemList.size() - 1);
        assertThat(testReceivedItem.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testReceivedItem.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testReceivedItem.getAssetIdSerial()).isEqualTo(DEFAULT_ASSET_ID_SERIAL);
        assertThat(testReceivedItem.getAssetIdMAC()).isEqualTo(DEFAULT_ASSET_ID_MAC);
        assertThat(testReceivedItem.getReceivedDate()).isEqualTo(DEFAULT_RECEIVED_DATE);
        assertThat(testReceivedItem.getTracked()).isEqualTo(DEFAULT_TRACKED);
        assertThat(testReceivedItem.getForInventory()).isEqualTo(DEFAULT_FOR_INVENTORY);
        assertThat(testReceivedItem.getBundleQuantity()).isEqualTo(UPDATED_BUNDLE_QUANTITY);
    }

    @Test
    @Transactional
    void fullUpdateReceivedItemWithPatch() throws Exception {
        // Initialize the database
        receivedItemRepository.saveAndFlush(receivedItem);

        int databaseSizeBeforeUpdate = receivedItemRepository.findAll().size();

        // Update the receivedItem using partial update
        ReceivedItem partialUpdatedReceivedItem = new ReceivedItem();
        partialUpdatedReceivedItem.setId(receivedItem.getId());

        partialUpdatedReceivedItem
            .note(UPDATED_NOTE)
            .location(UPDATED_LOCATION)
            .assetIdSerial(UPDATED_ASSET_ID_SERIAL)
            .assetIdMAC(UPDATED_ASSET_ID_MAC)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .tracked(UPDATED_TRACKED)
            .forInventory(UPDATED_FOR_INVENTORY)
            .bundleQuantity(UPDATED_BUNDLE_QUANTITY);

        restReceivedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceivedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceivedItem))
            )
            .andExpect(status().isOk());

        // Validate the ReceivedItem in the database
        List<ReceivedItem> receivedItemList = receivedItemRepository.findAll();
        assertThat(receivedItemList).hasSize(databaseSizeBeforeUpdate);
        ReceivedItem testReceivedItem = receivedItemList.get(receivedItemList.size() - 1);
        assertThat(testReceivedItem.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testReceivedItem.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testReceivedItem.getAssetIdSerial()).isEqualTo(UPDATED_ASSET_ID_SERIAL);
        assertThat(testReceivedItem.getAssetIdMAC()).isEqualTo(UPDATED_ASSET_ID_MAC);
        assertThat(testReceivedItem.getReceivedDate()).isEqualTo(UPDATED_RECEIVED_DATE);
        assertThat(testReceivedItem.getTracked()).isEqualTo(UPDATED_TRACKED);
        assertThat(testReceivedItem.getForInventory()).isEqualTo(UPDATED_FOR_INVENTORY);
        assertThat(testReceivedItem.getBundleQuantity()).isEqualTo(UPDATED_BUNDLE_QUANTITY);
    }

    @Test
    @Transactional
    void patchNonExistingReceivedItem() throws Exception {
        int databaseSizeBeforeUpdate = receivedItemRepository.findAll().size();
        receivedItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceivedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, receivedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receivedItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceivedItem in the database
        List<ReceivedItem> receivedItemList = receivedItemRepository.findAll();
        assertThat(receivedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReceivedItem() throws Exception {
        int databaseSizeBeforeUpdate = receivedItemRepository.findAll().size();
        receivedItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceivedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receivedItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceivedItem in the database
        List<ReceivedItem> receivedItemList = receivedItemRepository.findAll();
        assertThat(receivedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReceivedItem() throws Exception {
        int databaseSizeBeforeUpdate = receivedItemRepository.findAll().size();
        receivedItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceivedItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(receivedItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReceivedItem in the database
        List<ReceivedItem> receivedItemList = receivedItemRepository.findAll();
        assertThat(receivedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReceivedItem() throws Exception {
        // Initialize the database
        receivedItemRepository.saveAndFlush(receivedItem);

        int databaseSizeBeforeDelete = receivedItemRepository.findAll().size();

        // Delete the receivedItem
        restReceivedItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, receivedItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReceivedItem> receivedItemList = receivedItemRepository.findAll();
        assertThat(receivedItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
