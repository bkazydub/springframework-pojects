package com.shop.springframework.web;

import com.shop.springframework.domain.ShoppingCart;
import com.shop.springframework.domain.products.Product;
import com.shop.springframework.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@SessionAttributes("cart")
public class MainController {

    public static final String INDEX_VIEW = "products/index";
    public static final String LOGIN_VIEW = "signup/signup :: login";
    public static final String SIGNUP_VIEW = "signup/signup :: signup";
    public static final String CART_VIEW = "ajax/cart :: cart";
    public static final String ABOUT_VIEW = "about";

    private final ProductRepository productRepository;

    @Autowired
    public MainController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RequestMapping(value = "", method = GET)
    public String showIndexPage() {
        return INDEX_VIEW;
    }

    @RequestMapping(value = "/products", method = GET)
    public String showProductsByType(
            @RequestParam(value = "type", required = false) String type, Model model) {
        List<Product> products;
        try {
            if (type == null) type = "";
            products = (List<Product>) productRepository.findByType(Product.Type.valueOf(type.trim().toUpperCase()));
        } catch (IllegalArgumentException e) {
            products = productRepository.findAll();
        }
        model.addAttribute("products", products);
        return ProductController.PRODUCT_LIST_VIEW;
    }

    @RequestMapping(value = "cart")
    public String showCart(@ModelAttribute("cart") ShoppingCart cart, Model model) {
        model.addAttribute("cart", cart);
        return CART_VIEW;
    }

    @RequestMapping(value = "about")
    public String about() {
        return ABOUT_VIEW;
    }
}
