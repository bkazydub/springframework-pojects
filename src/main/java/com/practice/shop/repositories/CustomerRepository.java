package com.practice.shop.repositories;

import com.practice.shop.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    //Customer findByContactsEmail(String email);
}
