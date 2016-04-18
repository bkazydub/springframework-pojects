package com.shop.springframework.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
public class Address extends BaseEntity {

    @NotBlank @Column(nullable = false) protected String street;

    @NotBlank @Column(nullable = false) protected String city;

    @NotBlank @Column(nullable = false) protected String country;

    //@NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id", updatable = false, nullable = false)
    private Customer customer;

    /*@ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;*/

    public Address() {
    }

    public Address(String street, String city, String country) {

        this.street = street;
        this.city = city;
        this.country = country;
    }

    public Address getCopy() {
        return new Address(street, city, country);
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;
        if (!(object instanceof Address))
            return false;

        Address another = (Address) object;
        if (this.street.equalsIgnoreCase(another.getStreet()) &&
                this.city.equalsIgnoreCase(another.getCity()) &&
                this.country.equalsIgnoreCase(another.getCountry())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (street == null || city == null || country == null) {
            return 0;
        }
        int hash = 1;
        hash = hash * 13 + street.hashCode();
        hash = hash * 17 + city.hashCode();
        hash = hash * 31 + country.hashCode();
        return hash;
    }
}
