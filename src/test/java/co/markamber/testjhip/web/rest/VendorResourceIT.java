package co.markamber.testjhip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.markamber.testjhip.IntegrationTest;
import co.markamber.testjhip.domain.Vendor;
import co.markamber.testjhip.repository.VendorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VendorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VendorResourceIT {

    private static final String DEFAULT_LEGAL_ENTITY = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL_ENTITY = "BBBBBBBBBB";

    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";

    private static final String DEFAULT_BILLING_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_BILLING_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vendors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VendorRepository vendorRepository;

    @Mock
    private VendorRepository vendorRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVendorMockMvc;

    private Vendor vendor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendor createEntity(EntityManager em) {
        Vendor vendor = new Vendor().legalEntity(DEFAULT_LEGAL_ENTITY).nickname(DEFAULT_NICKNAME).billingAddress(DEFAULT_BILLING_ADDRESS);
        return vendor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendor createUpdatedEntity(EntityManager em) {
        Vendor vendor = new Vendor().legalEntity(UPDATED_LEGAL_ENTITY).nickname(UPDATED_NICKNAME).billingAddress(UPDATED_BILLING_ADDRESS);
        return vendor;
    }

    @BeforeEach
    public void initTest() {
        vendor = createEntity(em);
    }

    @Test
    @Transactional
    void createVendor() throws Exception {
        int databaseSizeBeforeCreate = vendorRepository.findAll().size();
        // Create the Vendor
        restVendorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendor)))
            .andExpect(status().isCreated());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeCreate + 1);
        Vendor testVendor = vendorList.get(vendorList.size() - 1);
        assertThat(testVendor.getLegalEntity()).isEqualTo(DEFAULT_LEGAL_ENTITY);
        assertThat(testVendor.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testVendor.getBillingAddress()).isEqualTo(DEFAULT_BILLING_ADDRESS);
    }

    @Test
    @Transactional
    void createVendorWithExistingId() throws Exception {
        // Create the Vendor with an existing ID
        vendor.setId(1L);

        int databaseSizeBeforeCreate = vendorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendor)))
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLegalEntityIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendorRepository.findAll().size();
        // set the field null
        vendor.setLegalEntity(null);

        // Create the Vendor, which fails.

        restVendorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendor)))
            .andExpect(status().isBadRequest());

        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVendors() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList
        restVendorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendor.getId().intValue())))
            .andExpect(jsonPath("$.[*].legalEntity").value(hasItem(DEFAULT_LEGAL_ENTITY)))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME)))
            .andExpect(jsonPath("$.[*].billingAddress").value(hasItem(DEFAULT_BILLING_ADDRESS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVendorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(vendorRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVendorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vendorRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVendorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vendorRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVendorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vendorRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get the vendor
        restVendorMockMvc
            .perform(get(ENTITY_API_URL_ID, vendor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vendor.getId().intValue()))
            .andExpect(jsonPath("$.legalEntity").value(DEFAULT_LEGAL_ENTITY))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME))
            .andExpect(jsonPath("$.billingAddress").value(DEFAULT_BILLING_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingVendor() throws Exception {
        // Get the vendor
        restVendorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();

        // Update the vendor
        Vendor updatedVendor = vendorRepository.findById(vendor.getId()).get();
        // Disconnect from session so that the updates on updatedVendor are not directly saved in db
        em.detach(updatedVendor);
        updatedVendor.legalEntity(UPDATED_LEGAL_ENTITY).nickname(UPDATED_NICKNAME).billingAddress(UPDATED_BILLING_ADDRESS);

        restVendorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVendor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVendor))
            )
            .andExpect(status().isOk());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
        Vendor testVendor = vendorList.get(vendorList.size() - 1);
        assertThat(testVendor.getLegalEntity()).isEqualTo(UPDATED_LEGAL_ENTITY);
        assertThat(testVendor.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testVendor.getBillingAddress()).isEqualTo(UPDATED_BILLING_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingVendor() throws Exception {
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();
        vendor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vendor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVendor() throws Exception {
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();
        vendor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVendor() throws Exception {
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();
        vendor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVendorWithPatch() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();

        // Update the vendor using partial update
        Vendor partialUpdatedVendor = new Vendor();
        partialUpdatedVendor.setId(vendor.getId());

        partialUpdatedVendor.legalEntity(UPDATED_LEGAL_ENTITY).billingAddress(UPDATED_BILLING_ADDRESS);

        restVendorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendor))
            )
            .andExpect(status().isOk());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
        Vendor testVendor = vendorList.get(vendorList.size() - 1);
        assertThat(testVendor.getLegalEntity()).isEqualTo(UPDATED_LEGAL_ENTITY);
        assertThat(testVendor.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testVendor.getBillingAddress()).isEqualTo(UPDATED_BILLING_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateVendorWithPatch() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();

        // Update the vendor using partial update
        Vendor partialUpdatedVendor = new Vendor();
        partialUpdatedVendor.setId(vendor.getId());

        partialUpdatedVendor.legalEntity(UPDATED_LEGAL_ENTITY).nickname(UPDATED_NICKNAME).billingAddress(UPDATED_BILLING_ADDRESS);

        restVendorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendor))
            )
            .andExpect(status().isOk());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
        Vendor testVendor = vendorList.get(vendorList.size() - 1);
        assertThat(testVendor.getLegalEntity()).isEqualTo(UPDATED_LEGAL_ENTITY);
        assertThat(testVendor.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testVendor.getBillingAddress()).isEqualTo(UPDATED_BILLING_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingVendor() throws Exception {
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();
        vendor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vendor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVendor() throws Exception {
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();
        vendor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVendor() throws Exception {
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();
        vendor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vendor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        int databaseSizeBeforeDelete = vendorRepository.findAll().size();

        // Delete the vendor
        restVendorMockMvc
            .perform(delete(ENTITY_API_URL_ID, vendor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
