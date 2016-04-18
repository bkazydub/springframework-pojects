package com.shop.springframework.repositories;

import com.shop.springframework.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("select o from Order o where o.customer.email = :email order by o.placed desc")
    List<Order> findByCustomerEmail(@Param("email") String email);

    //Order placeCartItemsToOrder(List<LineItem> item, Order order);

    // may need other name
    // TODO discard
    List<Order> findOrdersByCustomerFirstnameAndCustomerLastname(String firstname, String lastname);

    @Query("select o from Order o where o.id = ?1 and upper(o.customer.firstname) = upper(?2) and upper(o.customer.lastname) = upper(?3)")
    Order findOrder(Long orderId, String customerFirstname, String customerLastname);
}
