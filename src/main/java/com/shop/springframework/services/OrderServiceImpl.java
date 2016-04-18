package com.shop.springframework.services;

import com.shop.springframework.domain.order.LineItem;
import com.shop.springframework.domain.order.Order;
import com.shop.springframework.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public Order placeOrder(Order order) {
        for (LineItem item : order.getItems()) {
            item.setOrder(order);
        }
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    @Override
    public List<Order> findOrdersByCustomerFirstnameAndCustomerLastname(String firstname, String lastname) {
        return orderRepository.findOrdersByCustomerFirstnameAndCustomerLastname(firstname, lastname);
    }

    @Override
    public Order findOrder(Long orderId, String customerFirstname, String customerLastname) {
        return orderRepository.findOrder(orderId, customerFirstname, customerLastname);
    }
}
