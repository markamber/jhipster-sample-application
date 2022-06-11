package com.markamber.testjhip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderLineItem.
 */
@Entity
@Table(name = "order_line_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderLineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "expected_cost_unit", precision = 21, scale = 2)
    private BigDecimal expectedCostUnit;

    @Column(name = "sell_price_unit", precision = 21, scale = 2)
    private BigDecimal sellPriceUnit;

    @Column(name = "number_units")
    private Long numberUnits;

    @Column(name = "room")
    private String room;

    @Column(name = "system")
    private String system;

    @ManyToOne
    @JsonIgnoreProperties(value = { "manufacture", "purchaseOrderLineItems" }, allowSetters = true)
    private Product product;

    @JsonIgnoreProperties(value = { "fufill", "poLineItem" }, allowSetters = true)
    @OneToOne(mappedBy = "fufill")
    private ReceivedItem fufilledBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orderLineItems" }, allowSetters = true)
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderLineItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public OrderLineItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getExpectedCostUnit() {
        return this.expectedCostUnit;
    }

    public OrderLineItem expectedCostUnit(BigDecimal expectedCostUnit) {
        this.setExpectedCostUnit(expectedCostUnit);
        return this;
    }

    public void setExpectedCostUnit(BigDecimal expectedCostUnit) {
        this.expectedCostUnit = expectedCostUnit;
    }

    public BigDecimal getSellPriceUnit() {
        return this.sellPriceUnit;
    }

    public OrderLineItem sellPriceUnit(BigDecimal sellPriceUnit) {
        this.setSellPriceUnit(sellPriceUnit);
        return this;
    }

    public void setSellPriceUnit(BigDecimal sellPriceUnit) {
        this.sellPriceUnit = sellPriceUnit;
    }

    public Long getNumberUnits() {
        return this.numberUnits;
    }

    public OrderLineItem numberUnits(Long numberUnits) {
        this.setNumberUnits(numberUnits);
        return this;
    }

    public void setNumberUnits(Long numberUnits) {
        this.numberUnits = numberUnits;
    }

    public String getRoom() {
        return this.room;
    }

    public OrderLineItem room(String room) {
        this.setRoom(room);
        return this;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSystem() {
        return this.system;
    }

    public OrderLineItem system(String system) {
        this.setSystem(system);
        return this;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderLineItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public ReceivedItem getFufilledBy() {
        return this.fufilledBy;
    }

    public void setFufilledBy(ReceivedItem receivedItem) {
        if (this.fufilledBy != null) {
            this.fufilledBy.setFufill(null);
        }
        if (receivedItem != null) {
            receivedItem.setFufill(this);
        }
        this.fufilledBy = receivedItem;
    }

    public OrderLineItem fufilledBy(ReceivedItem receivedItem) {
        this.setFufilledBy(receivedItem);
        return this;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public OrderLineItem project(Project project) {
        this.setProject(project);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderLineItem)) {
            return false;
        }
        return id != null && id.equals(((OrderLineItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderLineItem{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", expectedCostUnit=" + getExpectedCostUnit() +
            ", sellPriceUnit=" + getSellPriceUnit() +
            ", numberUnits=" + getNumberUnits() +
            ", room='" + getRoom() + "'" +
            ", system='" + getSystem() + "'" +
            "}";
    }
}
