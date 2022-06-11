package com.markamber.testjhip.web.rest;

import com.markamber.testjhip.domain.Manufacture;
import com.markamber.testjhip.repository.ManufactureRepository;
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
 * REST controller for managing {@link com.markamber.testjhip.domain.Manufacture}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ManufactureResource {

    private final Logger log = LoggerFactory.getLogger(ManufactureResource.class);

    private static final String ENTITY_NAME = "manufacture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManufactureRepository manufactureRepository;

    public ManufactureResource(ManufactureRepository manufactureRepository) {
        this.manufactureRepository = manufactureRepository;
    }

    /**
     * {@code POST  /manufactures} : Create a new manufacture.
     *
     * @param manufacture the manufacture to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manufacture, or with status {@code 400 (Bad Request)} if the manufacture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manufactures")
    public ResponseEntity<Manufacture> createManufacture(@RequestBody Manufacture manufacture) throws URISyntaxException {
        log.debug("REST request to save Manufacture : {}", manufacture);
        if (manufacture.getId() != null) {
            throw new BadRequestAlertException("A new manufacture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Manufacture result = manufactureRepository.save(manufacture);
        return ResponseEntity
            .created(new URI("/api/manufactures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /manufactures/:id} : Updates an existing manufacture.
     *
     * @param id the id of the manufacture to save.
     * @param manufacture the manufacture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manufacture,
     * or with status {@code 400 (Bad Request)} if the manufacture is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manufacture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/manufactures/{id}")
    public ResponseEntity<Manufacture> updateManufacture(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Manufacture manufacture
    ) throws URISyntaxException {
        log.debug("REST request to update Manufacture : {}, {}", id, manufacture);
        if (manufacture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manufacture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manufactureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Manufacture result = manufactureRepository.save(manufacture);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manufacture.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /manufactures/:id} : Partial updates given fields of an existing manufacture, field will ignore if it is null
     *
     * @param id the id of the manufacture to save.
     * @param manufacture the manufacture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manufacture,
     * or with status {@code 400 (Bad Request)} if the manufacture is not valid,
     * or with status {@code 404 (Not Found)} if the manufacture is not found,
     * or with status {@code 500 (Internal Server Error)} if the manufacture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/manufactures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Manufacture> partialUpdateManufacture(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Manufacture manufacture
    ) throws URISyntaxException {
        log.debug("REST request to partial update Manufacture partially : {}, {}", id, manufacture);
        if (manufacture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manufacture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manufactureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Manufacture> result = manufactureRepository
            .findById(manufacture.getId())
            .map(existingManufacture -> {
                if (manufacture.getName() != null) {
                    existingManufacture.setName(manufacture.getName());
                }

                return existingManufacture;
            })
            .map(manufactureRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manufacture.getId().toString())
        );
    }

    /**
     * {@code GET  /manufactures} : get all the manufactures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manufactures in body.
     */
    @GetMapping("/manufactures")
    public List<Manufacture> getAllManufactures() {
        log.debug("REST request to get all Manufactures");
        return manufactureRepository.findAll();
    }

    /**
     * {@code GET  /manufactures/:id} : get the "id" manufacture.
     *
     * @param id the id of the manufacture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manufacture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/manufactures/{id}")
    public ResponseEntity<Manufacture> getManufacture(@PathVariable Long id) {
        log.debug("REST request to get Manufacture : {}", id);
        Optional<Manufacture> manufacture = manufactureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(manufacture);
    }

    /**
     * {@code DELETE  /manufactures/:id} : delete the "id" manufacture.
     *
     * @param id the id of the manufacture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/manufactures/{id}")
    public ResponseEntity<Void> deleteManufacture(@PathVariable Long id) {
        log.debug("REST request to delete Manufacture : {}", id);
        manufactureRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
