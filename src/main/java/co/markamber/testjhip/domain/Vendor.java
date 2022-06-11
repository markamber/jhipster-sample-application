package co.markamber.testjhip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vendor.
 */
@Entity
@Table(name = "vendor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vendor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "legal_entity", nullable = false)
    private String legalEntity;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "billing_address")
    private String billingAddress;

    @ManyToMany
    @JoinTable(
        name = "rel_vendor__manufactures",
        joinColumns = @JoinColumn(name = "vendor_id"),
        inverseJoinColumns = @JoinColumn(name = "manufactures_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "vendors" }, allowSetters = true)
    private Set<Manufacture> manufactures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vendor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLegalEntity() {
        return this.legalEntity;
    }

    public Vendor legalEntity(String legalEntity) {
        this.setLegalEntity(legalEntity);
        return this;
    }

    public void setLegalEntity(String legalEntity) {
        this.legalEntity = legalEntity;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Vendor nickname(String nickname) {
        this.setNickname(nickname);
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBillingAddress() {
        return this.billingAddress;
    }

    public Vendor billingAddress(String billingAddress) {
        this.setBillingAddress(billingAddress);
        return this;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Set<Manufacture> getManufactures() {
        return this.manufactures;
    }

    public void setManufactures(Set<Manufacture> manufactures) {
        this.manufactures = manufactures;
    }

    public Vendor manufactures(Set<Manufacture> manufactures) {
        this.setManufactures(manufactures);
        return this;
    }

    public Vendor addManufactures(Manufacture manufacture) {
        this.manufactures.add(manufacture);
        manufacture.getVendors().add(this);
        return this;
    }

    public Vendor removeManufactures(Manufacture manufacture) {
        this.manufactures.remove(manufacture);
        manufacture.getVendors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vendor)) {
            return false;
        }
        return id != null && id.equals(((Vendor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vendor{" +
            "id=" + getId() +
            ", legalEntity='" + getLegalEntity() + "'" +
            ", nickname='" + getNickname() + "'" +
            ", billingAddress='" + getBillingAddress() + "'" +
            "}";
    }
}
