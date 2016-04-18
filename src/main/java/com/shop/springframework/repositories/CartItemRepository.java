package com.shop.springframework.repositories;

import com.shop.springframework.domain.order.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<LineItem,Long> {
}
