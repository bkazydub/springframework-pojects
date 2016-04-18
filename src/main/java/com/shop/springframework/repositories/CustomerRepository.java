package com.shop.springframework.repositories;

import com.shop.springframework.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    //Customer findByContactsEmail(String email);
}
