package com.markamber.testjhip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.markamber.testjhip.domain.enumeration.ItemType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "model_name")
    private String modelName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ItemType type;

    @ManyToOne
    @JsonIgnoreProperties(value = { "products", "vendors" }, allowSetters = true)
    private Manufacture manufacture;

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "receivedItems", "product", "pos" }, allowSetters = true)
    private Set<PurchaseOrderLineItem> purchaseOrderLineItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return this.modelName;
    }

    public Product modelName(String modelName) {
        this.setModelName(modelName);
        return this;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public ItemType getType() {
        return this.type;
    }

    public Product type(ItemType type) {
        this.setType(type);
        return this;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public Manufacture getManufacture() {
        return this.manufacture;
    }

    public void setManufacture(Manufacture manufacture) {
        this.manufacture = manufacture;
    }

    public Product manufacture(Manufacture manufacture) {
        this.setManufacture(manufacture);
        return this;
    }

    public Set<PurchaseOrderLineItem> getPurchaseOrderLineItems() {
        return this.purchaseOrderLineItems;
    }

    public void setPurchaseOrderLineItems(Set<PurchaseOrderLineItem> purchaseOrderLineItems) {
        if (this.purchaseOrderLineItems != null) {
            this.purchaseOrderLineItems.forEach(i -> i.setProduct(null));
        }
        if (purchaseOrderLineItems != null) {
            purchaseOrderLineItems.forEach(i -> i.setProduct(this));
        }
        this.purchaseOrderLineItems = purchaseOrderLineItems;
    }

    public Product purchaseOrderLineItems(Set<PurchaseOrderLineItem> purchaseOrderLineItems) {
        this.setPurchaseOrderLineItems(purchaseOrderLineItems);
        return this;
    }

    public Product addPurchaseOrderLineItem(PurchaseOrderLineItem purchaseOrderLineItem) {
        this.purchaseOrderLineItems.add(purchaseOrderLineItem);
        purchaseOrderLineItem.setProduct(this);
        return this;
    }

    public Product removePurchaseOrderLineItem(PurchaseOrderLineItem purchaseOrderLineItem) {
        this.purchaseOrderLineItems.remove(purchaseOrderLineItem);
        purchaseOrderLineItem.setProduct(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", modelName='" + getModelName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
