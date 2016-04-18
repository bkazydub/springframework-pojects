package com.shop.springframework.signup;

import com.shop.springframework.domain.Address;
import com.shop.springframework.domain.Customer;
import com.shop.springframework.domain.account.Account;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;

/*Acts as a DTO*/
public class SignupForm {

    @NotBlank
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

    // Used in SignupControllerTest class
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof SignupForm))
            return false;
        SignupForm another = (SignupForm) obj;
        if (this == another)
            return true;
        if (this.customer.getEmail() == another.getCustomer().getEmail() && this.password == another.getPassword())
            return true;
        return false;
    }

    public Account createAccount() {
        Account account = new Account(customer.getEmail(), password, "ROLE_USER");
        account.setCustomer(customer);
        return account;
    }
}
