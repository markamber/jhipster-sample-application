package co.markamber.testjhip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReceivedItem.
 */
@Entity
@Table(name = "received_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReceivedItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "note")
    private String note;

    @Column(name = "location")
    private String location;

    @Column(name = "asset_id_serial")
    private String assetIdSerial;

    @Column(name = "asset_id_mac")
    private String assetIdMAC;

    @Column(name = "received_date")
    private LocalDate receivedDate;

    @JsonIgnoreProperties(value = { "product", "fufilledBy", "project" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private OrderLineItem fufill;

    @ManyToOne
    @JsonIgnoreProperties(value = { "receivedItems", "product", "pos" }, allowSetters = true)
    private PurchaseOrderLineItem poLineItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReceivedItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return this.note;
    }

    public ReceivedItem note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLocation() {
        return this.location;
    }

    public ReceivedItem location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAssetIdSerial() {
        return this.assetIdSerial;
    }

    public ReceivedItem assetIdSerial(String assetIdSerial) {
        this.setAssetIdSerial(assetIdSerial);
        return this;
    }

    public void setAssetIdSerial(String assetIdSerial) {
        this.assetIdSerial = assetIdSerial;
    }

    public String getAssetIdMAC() {
        return this.assetIdMAC;
    }

    public ReceivedItem assetIdMAC(String assetIdMAC) {
        this.setAssetIdMAC(assetIdMAC);
        return this;
    }

    public void setAssetIdMAC(String assetIdMAC) {
        this.assetIdMAC = assetIdMAC;
    }

    public LocalDate getReceivedDate() {
        return this.receivedDate;
    }

    public ReceivedItem receivedDate(LocalDate receivedDate) {
        this.setReceivedDate(receivedDate);
        return this;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public OrderLineItem getFufill() {
        return this.fufill;
    }

    public void setFufill(OrderLineItem orderLineItem) {
        this.fufill = orderLineItem;
    }

    public ReceivedItem fufill(OrderLineItem orderLineItem) {
        this.setFufill(orderLineItem);
        return this;
    }

    public PurchaseOrderLineItem getPoLineItem() {
        return this.poLineItem;
    }

    public void setPoLineItem(PurchaseOrderLineItem purchaseOrderLineItem) {
        this.poLineItem = purchaseOrderLineItem;
    }

    public ReceivedItem poLineItem(PurchaseOrderLineItem purchaseOrderLineItem) {
        this.setPoLineItem(purchaseOrderLineItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReceivedItem)) {
            return false;
        }
        return id != null && id.equals(((ReceivedItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReceivedItem{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            ", location='" + getLocation() + "'" +
            ", assetIdSerial='" + getAssetIdSerial() + "'" +
            ", assetIdMAC='" + getAssetIdMAC() + "'" +
            ", receivedDate='" + getReceivedDate() + "'" +
            "}";
    }
}
