package com.practice.shop.repositories;

import com.practice.shop.domain.order.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<LineItem,Long> {
}
