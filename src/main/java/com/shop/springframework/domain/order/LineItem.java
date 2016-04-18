package com.shop.springframework.domain.order;

import com.shop.springframework.domain.BaseEntity;
import com.shop.springframework.domain.products.Product;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Entity
public class LineItem extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    protected LineItem() {
    }

    public LineItem(Product product) {
        this(product, 1);
    }

    public LineItem(Product product, int quantity) {

        Assert.notNull(product, "The given product must not be null!");
        Assert.isTrue(quantity > 0, "The quantity of products to be bought must be greater than 0!");

        this.product = product;
        this.quantity = quantity;
        this.price = product.getPrice();
    }

    @Transient
    public BigDecimal getTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public void increaseAmount() {
        increaseAmount(1);
    }

    public void increaseAmount(int increment) {
        Assert.isTrue(increment > 0);
        quantity += increment;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
