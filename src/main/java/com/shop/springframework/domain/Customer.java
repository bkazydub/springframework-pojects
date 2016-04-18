package com.shop.springframework.domain;

import com.shop.springframework.domain.account.Account;
import com.shop.springframework.domain.order.Order;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

@Entity
public class Customer extends BaseEntity {

    @NotBlank
    @Column(name = "first_name")
    private String firstname;

    @NotBlank
    @Column(name = "last_name")
    private String lastname;

    @OneToOne(mappedBy = "customer")
    private Account account;

    @OneToMany(mappedBy = "customer", cascade = REMOVE, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(cascade = {PERSIST, MERGE, REMOVE},
            orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "customer")
    private Set<Address> addresses = new HashSet<>();

    @Email protected String email;

    @NotBlank
    @Pattern(regexp = "\\d{10}")
    protected String phoneNumber;

    @Transient
    private boolean loggedIn;

    public Customer() {
    }

    public Customer(String firstname, String lastname) {

        Assert.hasText(firstname);
        Assert.hasText(lastname);

        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Order add(Order order) {
        orders.add(order);
        return order;
    }

    public Address add(Address address) {
        addresses.add(address);
        return address;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<Address> getAddresses() {
        return Collections.unmodifiableSet(addresses);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}