package com.shop.springframework.web;

import com.shop.springframework.domain.order.LineItem;
import com.shop.springframework.domain.products.Product;
import com.shop.springframework.domain.ShoppingCart;
import com.shop.springframework.domain.products.ProductUtil;
import com.shop.springframework.repositories.ProductRepository;
import com.shop.springframework.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/products")
@SessionAttributes("cart")
public class ProductController {

    private final ProductRepository repository;
    private final OrderService orderService;

    public static final String PRODUCT_CREATE_OR_UPDATE_VIEW = "db/createOrUpdateProductForm";
    public static final String PRODUCT_INFO_VIEW = "products/info";
    public static final String PRODUCT_LIST_VIEW = "products/list";
    public static final String PRODUCT_FIND_RESULT_VIEW = "products/results";

    @Autowired
    public ProductController(ProductRepository repository,
                             OrderService orderService) {
        this.repository = repository;
        this.orderService = orderService;
    }

    @RequestMapping(value = "/db/new", method = GET)
    public String showProductForm(Model model) {
        model.addAttribute("product", new Product());
        return PRODUCT_CREATE_OR_UPDATE_VIEW;
    }

    @RequestMapping(value = "/db/new", method = POST)
    public String processProductForm(@Valid Product product, BindingResult result) {
        if (result.hasErrors()) {
            return PRODUCT_CREATE_OR_UPDATE_VIEW;
        } else {
            Product saved = repository.save(product);
            return "redirect:/products/info/" + saved.getId();
        }
    }

    @RequestMapping(value = "/info/{id}", method = GET)
    public String showInfoPage(@PathVariable("id") Product product, Model model) {
        model.addAttribute("product", product);
        return PRODUCT_INFO_VIEW;
    }

    @RequestMapping(value = "/db/edit/{id}", method = GET)
    public String showEditForm(@PathVariable("id") Product product, Model model) {
        model.addAttribute("product", product);
        return PRODUCT_CREATE_OR_UPDATE_VIEW;
    }

    @RequestMapping(value = "/db/edit/{id}", method = PUT)
    public String processEditForm(@Valid Product product, BindingResult result) {
        if (result.hasErrors()) {
            return PRODUCT_CREATE_OR_UPDATE_VIEW;
        } else {
            repository.save(product);
            return "redirect:/products/info/{id}";
        }
    }

    @RequestMapping(value = "/find", method = GET)
    public String showFindResult(@RequestParam("query") String query, Model model) {
        if (query == null || query.isEmpty()) {
            model.addAttribute("shortQuery");
        } else {
            model.addAttribute("results", repository.findByNameLikeIgnoreCase("%" + query + "%"));
        }
        return PRODUCT_FIND_RESULT_VIEW;
    }

    @RequestMapping(value = "/addToCart/{productId}", method = POST)
    public String addToCart(@PathVariable("productId") Product product,
                            @ModelAttribute("cart") ShoppingCart cart,
                            RedirectAttributes redirect) {
        LineItem item = new LineItem(product, 1);
        cart.addProduct(item);
        redirect.addFlashAttribute("infoMessage", "Item " + product.getName() + " was added to your cart.");

        return "fragments/bodyHeader :: bodyHeader";
    }

    @RequestMapping(value = "/removeFromCart/{productId}", method = POST)
    public String removeFromCart(@PathVariable("productId") Product product,
                                 @ModelAttribute("cart") ShoppingCart cart) {
        cart.removeProduct(product);
        return MainController.CART_VIEW;
    }

    @RequestMapping(value = "/login/withAjaxDemo", method = GET)
    public String ajaxNoMoreNoLess() {
        return "fragments/authorizationRelated :: loginForm";
    }

    @RequestMapping(value = "/fetchTypeParams/{type}", method = POST)
    public String renderTypeCharacteristics(@PathVariable("type") Product.Type type,
                                            @ModelAttribute Product product, Model model) {
        String[] techArray = null;
        if (type == Product.Type.LAPTOP) {
            techArray = ProductUtil.LAPTOP_REQUIRED_PROPERTIES;
        } else if (type == Product.Type.PHONE) {
            techArray = ProductUtil.MOBILE_REQUIRED_PROPERTIES;
        } else if (type == Product.Type.ACCESSORIES) {
            techArray = ProductUtil.ACCESSORIES_REQUIRED_PROPERTIES;
        }
        product.setTechnicalCharacteristics(ProductUtil.createCharacteristicsMap(techArray));
        model.addAttribute("techArray", techArray);
        model.addAttribute("product", product);
        return "fragments/characteristics :: characteristics";
    }
}
