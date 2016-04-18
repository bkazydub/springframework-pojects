package com.shop.springframework.domain.order;

import com.shop.springframework.domain.Address;
import com.shop.springframework.domain.BaseEntity;
import com.shop.springframework.domain.Customer;
import com.shop.springframework.support.LocalDateTimePersistenceConverter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "Orders")
public class Order extends BaseEntity {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PROCESSED = "PROCESSED";
    public static final String STATUS_SHIPPING = "SHIPPING";
    public static final String STATUS_SHIPPED = "SHIPPED";

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(cascade = {MERGE, REMOVE})
    private Address shippingAddress;

    @NotEmpty
    @OneToMany(mappedBy = "order", cascade = {PERSIST, REMOVE, MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LineItem> items = new ArrayList<>(); // or Set?

    @Column(nullable = false, updatable = false)
    @DateTimeFormat(pattern = "dd/mm/yyyy hh:mm:ss")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime placed;

    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime received;

    @Column(nullable = false)
    private String status;


    protected Order() {
    }

    public Order(Customer customer, Address shippingAddress, List<LineItem> items) {

        Assert.notNull(customer);

        this.shippingAddress = shippingAddress;
        this.items = items;
        this.customer = customer;
    }

    @PrePersist
    public void init() {
        if (placed == null) {
            placed = LocalDateTime.now();
        }
        if (status == null) {
            status = STATUS_PENDING;
        }
    }

    public void add(LineItem lineItem) {
        items.add(lineItem);
    }

    @Transient
    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (LineItem item : items) {
            total = total.add(item.getTotal());
        }
        return total;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<LineItem> getItems() {
        return items;
    }

    public void setItems(List<LineItem> items) {
        this.items = items;
    }

    public LocalDateTime getPlaced() {
        return placed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getReceived() {
        return received;
    }

    public void setReceived(LocalDateTime received) {
        this.received = received;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }
}
