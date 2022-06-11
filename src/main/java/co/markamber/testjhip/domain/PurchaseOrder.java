package co.markamber.testjhip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PurchaseOrder.
 */
@Entity
@Table(name = "purchase_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ship_to")
    private String shipTo;

    @Column(name = "notes")
    private String notes;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JsonIgnoreProperties(value = { "receivedItems", "product", "pos" }, allowSetters = true)
    private PurchaseOrderLineItem poLineItems;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PurchaseOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShipTo() {
        return this.shipTo;
    }

    public PurchaseOrder shipTo(String shipTo) {
        this.setShipTo(shipTo);
        return this;
    }

    public void setShipTo(String shipTo) {
        this.shipTo = shipTo;
    }

    public String getNotes() {
        return this.notes;
    }

    public PurchaseOrder notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public PurchaseOrder date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public PurchaseOrderLineItem getPoLineItems() {
        return this.poLineItems;
    }

    public void setPoLineItems(PurchaseOrderLineItem purchaseOrderLineItem) {
        this.poLineItems = purchaseOrderLineItem;
    }

    public PurchaseOrder poLineItems(PurchaseOrderLineItem purchaseOrderLineItem) {
        this.setPoLineItems(purchaseOrderLineItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseOrder)) {
            return false;
        }
        return id != null && id.equals(((PurchaseOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrder{" +
            "id=" + getId() +
            ", shipTo='" + getShipTo() + "'" +
            ", notes='" + getNotes() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
