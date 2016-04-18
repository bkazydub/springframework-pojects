package com.shop.springframework.domain;

import com.shop.springframework.domain.order.LineItem;
import com.shop.springframework.domain.products.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart implements Serializable {

    private List<LineItem> contents = new ArrayList<>();

    public LineItem addProduct(LineItem item) {
        int i = indexOf(item);
        if (i == -1) {
            contents.add(item);
        } else {
            contents.get(i).increaseAmount();
        }
        return item;
    }

    private int indexOf(LineItem item) {
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).getProduct().getName().equals(item.getProduct().getName())) {
                return i;
            }
        }
        return -1;
    }

    public void removeProduct(Product product) {
        int i = indexOf(product);
        if (i != -1) {
            contents.remove(i);
        }
    }

    private int indexOf(Product product) {
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).getProduct().getId().equals(product.getId())) {
                return i;
            }
        }
        return -1;
    }

    public List<LineItem> getContents() {
        return contents;
    }

    public int getNumberOfProducts() {
        return contents.size();
    }

    public BigDecimal getTotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (LineItem item : contents) {
            sum = sum.add(item.getTotal());
        }
        return sum;
    }
}
