package co.markamber.testjhip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.markamber.testjhip.IntegrationTest;
import co.markamber.testjhip.domain.Manufacture;
import co.markamber.testjhip.repository.ManufactureRepository;
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
 * Integration tests for the {@link ManufactureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ManufactureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/manufactures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ManufactureRepository manufactureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManufactureMockMvc;

    private Manufacture manufacture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manufacture createEntity(EntityManager em) {
        Manufacture manufacture = new Manufacture().name(DEFAULT_NAME);
        return manufacture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manufacture createUpdatedEntity(EntityManager em) {
        Manufacture manufacture = new Manufacture().name(UPDATED_NAME);
        return manufacture;
    }

    @BeforeEach
    public void initTest() {
        manufacture = createEntity(em);
    }

    @Test
    @Transactional
    void createManufacture() throws Exception {
        int databaseSizeBeforeCreate = manufactureRepository.findAll().size();
        // Create the Manufacture
        restManufactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manufacture)))
            .andExpect(status().isCreated());

        // Validate the Manufacture in the database
        List<Manufacture> manufactureList = manufactureRepository.findAll();
        assertThat(manufactureList).hasSize(databaseSizeBeforeCreate + 1);
        Manufacture testManufacture = manufactureList.get(manufactureList.size() - 1);
        assertThat(testManufacture.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createManufactureWithExistingId() throws Exception {
        // Create the Manufacture with an existing ID
        manufacture.setId(1L);

        int databaseSizeBeforeCreate = manufactureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restManufactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manufacture)))
            .andExpect(status().isBadRequest());

        // Validate the Manufacture in the database
        List<Manufacture> manufactureList = manufactureRepository.findAll();
        assertThat(manufactureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllManufactures() throws Exception {
        // Initialize the database
        manufactureRepository.saveAndFlush(manufacture);

        // Get all the manufactureList
        restManufactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manufacture.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getManufacture() throws Exception {
        // Initialize the database
        manufactureRepository.saveAndFlush(manufacture);

        // Get the manufacture
        restManufactureMockMvc
            .perform(get(ENTITY_API_URL_ID, manufacture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manufacture.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingManufacture() throws Exception {
        // Get the manufacture
        restManufactureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewManufacture() throws Exception {
        // Initialize the database
        manufactureRepository.saveAndFlush(manufacture);

        int databaseSizeBeforeUpdate = manufactureRepository.findAll().size();

        // Update the manufacture
        Manufacture updatedManufacture = manufactureRepository.findById(manufacture.getId()).get();
        // Disconnect from session so that the updates on updatedManufacture are not directly saved in db
        em.detach(updatedManufacture);
        updatedManufacture.name(UPDATED_NAME);

        restManufactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedManufacture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedManufacture))
            )
            .andExpect(status().isOk());

        // Validate the Manufacture in the database
        List<Manufacture> manufactureList = manufactureRepository.findAll();
        assertThat(manufactureList).hasSize(databaseSizeBeforeUpdate);
        Manufacture testManufacture = manufactureList.get(manufactureList.size() - 1);
        assertThat(testManufacture.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingManufacture() throws Exception {
        int databaseSizeBeforeUpdate = manufactureRepository.findAll().size();
        manufacture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManufactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manufacture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manufacture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufacture in the database
        List<Manufacture> manufactureList = manufactureRepository.findAll();
        assertThat(manufactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchManufacture() throws Exception {
        int databaseSizeBeforeUpdate = manufactureRepository.findAll().size();
        manufacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manufacture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufacture in the database
        List<Manufacture> manufactureList = manufactureRepository.findAll();
        assertThat(manufactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManufacture() throws Exception {
        int databaseSizeBeforeUpdate = manufactureRepository.findAll().size();
        manufacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufactureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manufacture)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manufacture in the database
        List<Manufacture> manufactureList = manufactureRepository.findAll();
        assertThat(manufactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateManufactureWithPatch() throws Exception {
        // Initialize the database
        manufactureRepository.saveAndFlush(manufacture);

        int databaseSizeBeforeUpdate = manufactureRepository.findAll().size();

        // Update the manufacture using partial update
        Manufacture partialUpdatedManufacture = new Manufacture();
        partialUpdatedManufacture.setId(manufacture.getId());

        restManufactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManufacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManufacture))
            )
            .andExpect(status().isOk());

        // Validate the Manufacture in the database
        List<Manufacture> manufactureList = manufactureRepository.findAll();
        assertThat(manufactureList).hasSize(databaseSizeBeforeUpdate);
        Manufacture testManufacture = manufactureList.get(manufactureList.size() - 1);
        assertThat(testManufacture.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateManufactureWithPatch() throws Exception {
        // Initialize the database
        manufactureRepository.saveAndFlush(manufacture);

        int databaseSizeBeforeUpdate = manufactureRepository.findAll().size();

        // Update the manufacture using partial update
        Manufacture partialUpdatedManufacture = new Manufacture();
        partialUpdatedManufacture.setId(manufacture.getId());

        partialUpdatedManufacture.name(UPDATED_NAME);

        restManufactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManufacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManufacture))
            )
            .andExpect(status().isOk());

        // Validate the Manufacture in the database
        List<Manufacture> manufactureList = manufactureRepository.findAll();
        assertThat(manufactureList).hasSize(databaseSizeBeforeUpdate);
        Manufacture testManufacture = manufactureList.get(manufactureList.size() - 1);
        assertThat(testManufacture.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingManufacture() throws Exception {
        int databaseSizeBeforeUpdate = manufactureRepository.findAll().size();
        manufacture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManufactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, manufacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manufacture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufacture in the database
        List<Manufacture> manufactureList = manufactureRepository.findAll();
        assertThat(manufactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManufacture() throws Exception {
        int databaseSizeBeforeUpdate = manufactureRepository.findAll().size();
        manufacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manufacture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufacture in the database
        List<Manufacture> manufactureList = manufactureRepository.findAll();
        assertThat(manufactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManufacture() throws Exception {
        int databaseSizeBeforeUpdate = manufactureRepository.findAll().size();
        manufacture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufactureMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(manufacture))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manufacture in the database
        List<Manufacture> manufactureList = manufactureRepository.findAll();
        assertThat(manufactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteManufacture() throws Exception {
        // Initialize the database
        manufactureRepository.saveAndFlush(manufacture);

        int databaseSizeBeforeDelete = manufactureRepository.findAll().size();

        // Delete the manufacture
        restManufactureMockMvc
            .perform(delete(ENTITY_API_URL_ID, manufacture.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Manufacture> manufactureList = manufactureRepository.findAll();
        assertThat(manufactureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
