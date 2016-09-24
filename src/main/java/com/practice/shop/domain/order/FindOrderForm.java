package com.practice.shop.domain.order;

/**
 * Created by dragonmf on 4/11/16.
 */
public class FindOrderForm {

    private Long orderId;
    private String firstname;
    private String lastname;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
