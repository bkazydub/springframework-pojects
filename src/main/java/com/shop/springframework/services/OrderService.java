package com.shop.springframework.services;

import com.shop.springframework.domain.order.Order;

import java.util.List;

public interface OrderService {

    Order placeOrder(Order order);

    List<Order> findByCustomerEmail(String email);

    List<Order> findOrdersByCustomerFirstnameAndCustomerLastname(String firstname, String lastname);

    Order findOrder(Long orderId, String customerFirstname, String customerLastname);
}
