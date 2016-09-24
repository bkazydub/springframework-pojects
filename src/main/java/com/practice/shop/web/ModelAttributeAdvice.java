package com.practice.shop.web;

import com.practice.shop.domain.ShoppingCart;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@ControllerAdvice
@SessionAttributes("cart")
public class ModelAttributeAdvice {

    @ModelAttribute("cart")
    public ShoppingCart getShoppingCart() {
        return new ShoppingCart();
    }
}
