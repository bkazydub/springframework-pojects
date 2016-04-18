package com.shop.springframework.repositories;

import com.shop.springframework.domain.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional(readOnly = true)
public interface ProductRepository extends JpaRepository<Product,Long> {

    // TODO findDistinctByNameOrDescriptionLike
    Collection<Product> findByNameLike(String name);

    Collection<Product> findByType(Product.Type type);
}
