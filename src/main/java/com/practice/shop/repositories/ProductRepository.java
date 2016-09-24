package com.practice.shop.repositories;

import com.practice.shop.domain.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional(readOnly = true)
public interface ProductRepository extends JpaRepository<Product,Long> {

    Collection<Product> findByNameLikeIgnoreCase(String name);
    Collection<Product> findByType(Product.Type type);
}
