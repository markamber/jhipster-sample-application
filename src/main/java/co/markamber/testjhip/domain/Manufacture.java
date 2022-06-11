package co.markamber.testjhip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Manufacture.
 */
@Entity
@Table(name = "manufacture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Manufacture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "manufacture")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "manufacture", "purchaseOrderLineItems" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @ManyToMany(mappedBy = "manufactures")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "manufactures" }, allowSetters = true)
    private Set<Vendor> vendors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Manufacture id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Manufacture name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setManufacture(null));
        }
        if (products != null) {
            products.forEach(i -> i.setManufacture(this));
        }
        this.products = products;
    }

    public Manufacture products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Manufacture addProduct(Product product) {
        this.products.add(product);
        product.setManufacture(this);
        return this;
    }

    public Manufacture removeProduct(Product product) {
        this.products.remove(product);
        product.setManufacture(null);
        return this;
    }

    public Set<Vendor> getVendors() {
        return this.vendors;
    }

    public void setVendors(Set<Vendor> vendors) {
        if (this.vendors != null) {
            this.vendors.forEach(i -> i.removeManufactures(this));
        }
        if (vendors != null) {
            vendors.forEach(i -> i.addManufactures(this));
        }
        this.vendors = vendors;
    }

    public Manufacture vendors(Set<Vendor> vendors) {
        this.setVendors(vendors);
        return this;
    }

    public Manufacture addVendors(Vendor vendor) {
        this.vendors.add(vendor);
        vendor.getManufactures().add(this);
        return this;
    }

    public Manufacture removeVendors(Vendor vendor) {
        this.vendors.remove(vendor);
        vendor.getManufactures().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Manufacture)) {
            return false;
        }
        return id != null && id.equals(((Manufacture) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Manufacture{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
