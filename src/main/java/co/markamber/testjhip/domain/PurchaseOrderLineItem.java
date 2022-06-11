package co.markamber.testjhip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PurchaseOrderLineItem.
 */
@Entity
@Table(name = "purchase_order_line_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PurchaseOrderLineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "note")
    private String note;

    @Column(name = "estimated_ship_date")
    private LocalDate estimatedShipDate;

    @OneToMany(mappedBy = "poLineItem")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "fufill", "poLineItem" }, allowSetters = true)
    private Set<ReceivedItem> receivedItems = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "manufacture", "purchaseOrderLineItems" }, allowSetters = true)
    private Product product;

    @OneToMany(mappedBy = "poLineItems")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "poLineItems" }, allowSetters = true)
    private Set<PurchaseOrder> pos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PurchaseOrderLineItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return this.note;
    }

    public PurchaseOrderLineItem note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getEstimatedShipDate() {
        return this.estimatedShipDate;
    }

    public PurchaseOrderLineItem estimatedShipDate(LocalDate estimatedShipDate) {
        this.setEstimatedShipDate(estimatedShipDate);
        return this;
    }

    public void setEstimatedShipDate(LocalDate estimatedShipDate) {
        this.estimatedShipDate = estimatedShipDate;
    }

    public Set<ReceivedItem> getReceivedItems() {
        return this.receivedItems;
    }

    public void setReceivedItems(Set<ReceivedItem> receivedItems) {
        if (this.receivedItems != null) {
            this.receivedItems.forEach(i -> i.setPoLineItem(null));
        }
        if (receivedItems != null) {
            receivedItems.forEach(i -> i.setPoLineItem(this));
        }
        this.receivedItems = receivedItems;
    }

    public PurchaseOrderLineItem receivedItems(Set<ReceivedItem> receivedItems) {
        this.setReceivedItems(receivedItems);
        return this;
    }

    public PurchaseOrderLineItem addReceivedItems(ReceivedItem receivedItem) {
        this.receivedItems.add(receivedItem);
        receivedItem.setPoLineItem(this);
        return this;
    }

    public PurchaseOrderLineItem removeReceivedItems(ReceivedItem receivedItem) {
        this.receivedItems.remove(receivedItem);
        receivedItem.setPoLineItem(null);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PurchaseOrderLineItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Set<PurchaseOrder> getPos() {
        return this.pos;
    }

    public void setPos(Set<PurchaseOrder> purchaseOrders) {
        if (this.pos != null) {
            this.pos.forEach(i -> i.setPoLineItems(null));
        }
        if (purchaseOrders != null) {
            purchaseOrders.forEach(i -> i.setPoLineItems(this));
        }
        this.pos = purchaseOrders;
    }

    public PurchaseOrderLineItem pos(Set<PurchaseOrder> purchaseOrders) {
        this.setPos(purchaseOrders);
        return this;
    }

    public PurchaseOrderLineItem addPo(PurchaseOrder purchaseOrder) {
        this.pos.add(purchaseOrder);
        purchaseOrder.setPoLineItems(this);
        return this;
    }

    public PurchaseOrderLineItem removePo(PurchaseOrder purchaseOrder) {
        this.pos.remove(purchaseOrder);
        purchaseOrder.setPoLineItems(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseOrderLineItem)) {
            return false;
        }
        return id != null && id.equals(((PurchaseOrderLineItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrderLineItem{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            ", estimatedShipDate='" + getEstimatedShipDate() + "'" +
            "}";
    }
}
