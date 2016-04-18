package com.shop.springframework.domain.order;

import com.shop.springframework.domain.Address;
import com.shop.springframework.domain.Customer;

import javax.validation.Valid;

/**
 * For new Customer.
 */
public class NewCheckoutForm {

    @Valid
    private Customer customer = new Customer();

    @Valid
    private Address address = new Address();

    public Customer getCustomer() {
        return customer;
    }

    public Address getAddress() {
        return address;
    }
}
