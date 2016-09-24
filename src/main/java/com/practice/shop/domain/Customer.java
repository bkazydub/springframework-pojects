package com.practice.shop.domain;

import com.practice.shop.domain.account.Account;
import com.practice.shop.domain.order.Order;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

import static javax.persistence.CascadeType.*;

@Entity
public class Customer extends BaseEntity {

    @NotBlank
    @Column(name = "first_name", nullable = false)
    @Size(min = 2, max = 40)
    private String firstname;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    @Size(min = 2, max = 40)
    private String lastname;

    @OneToOne(mappedBy = "customer")
    private Account account;

    @OneToMany(mappedBy = "customer", cascade = REMOVE, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(cascade = {PERSIST, MERGE, REMOVE},
            orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "customer")
    private Set<Address> addresses = new HashSet<>();

    @NotBlank
    @Email @Column(nullable = false)
    protected String email;

    @Pattern(regexp = "\\d{10}")
    @Column(name = "phone_number")
    @Size(min = 10, max = 10)
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
