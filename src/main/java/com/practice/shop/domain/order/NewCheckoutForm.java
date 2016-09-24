package com.practice.shop.domain.order;

import com.practice.shop.domain.Address;
import com.practice.shop.domain.Customer;

import javax.validation.Valid;

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
