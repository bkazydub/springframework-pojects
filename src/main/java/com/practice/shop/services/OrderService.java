package com.practice.shop.services;

import com.practice.shop.domain.order.Order;

import java.util.List;

public interface OrderService {

    Order placeOrder(Order order);

    List<Order> findByCustomerEmail(String email);

    List<Order> findOrdersByCustomerFirstnameAndCustomerLastname(String firstname, String lastname);

    Order findOrder(Long orderId, String customerFirstname, String customerLastname);
}
