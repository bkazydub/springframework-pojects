package com.practice.shop.signup;

import com.practice.shop.domain.account.Account;
import com.practice.shop.domain.Address;
import com.practice.shop.domain.Customer;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * Backing bean for sign up form.
 */
public class SignupForm {

    @NotBlank
    @Size(min = 6, max = 32)
    private String password;

    @Valid
    private Customer customer = new Customer();

    @Valid
    private Address address = new Address();

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof SignupForm))
            return false;
        SignupForm another = (SignupForm) obj;
        if (this == another)
            return true;
        String email = customer.getEmail() == null ? "" : customer.getEmail();
        String anotherEmail = another.customer.getEmail();
        anotherEmail = anotherEmail == null ? "" : anotherEmail;
        String password = this.password == null ? "" : this.password;
        String anotherPassword = another.password == null ? "" : another.password;
        if (email.equals(anotherEmail) && password.equals(anotherPassword))
            return true;
        return false;
    }

    public Account createAccount() {
        Account account = new Account(customer.getEmail(), password, "ROLE_USER");
        account.setCustomer(customer);
        return account;
    }
}
