package com.practice.shop.domain.account;

import com.practice.shop.domain.BaseEntity;
import com.practice.shop.domain.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;

import static javax.persistence.CascadeType.PERSIST;

@Entity
@NamedQuery(name = Account.FIND_BY_EMAIL, query = "select a from Account a where a.email = :email")
public class Account extends BaseEntity {

    public static final String FIND_BY_EMAIL = "Account.findByEmail";

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    @NotNull
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;

    private String role = ROLE_USER;

    @NotNull
    @OneToOne(cascade = PERSIST)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    protected Account() {
    }

    public Account(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(getRole()));
    }

    public boolean isAdmin() {
        return this.role.equals(ROLE_ADMIN);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
