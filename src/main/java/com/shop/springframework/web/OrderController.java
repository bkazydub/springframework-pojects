package com.shop.springframework.web;

import com.shop.springframework.domain.Address;
import com.shop.springframework.domain.Customer;
import com.shop.springframework.domain.order.Order;
import com.shop.springframework.domain.ShoppingCart;
import com.shop.springframework.domain.account.UserService;
import com.shop.springframework.domain.order.NewCheckoutForm;
import com.shop.springframework.repositories.AddressRepository;
import com.shop.springframework.repositories.CustomerRepository;
import com.shop.springframework.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("order")
@SessionAttributes("cart")
public class OrderController {

    public static final String ORDER_METHOD_SELECTION_VIEW = "orders/selectMethod";
    public static final String ORDER_CHECKOUT_VIEW = "orders/checkout";
    public static final String ORDER_LIST_VIEW = "orders/list";
    public static final String ORDER_FIND_VIEW = "orders/find";

    private final OrderService service;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public OrderController(OrderService service, CustomerRepository customerRepository,
                           AddressRepository addressRepository) {
        this.service = service;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    @RequestMapping("/selectMethod")
    public String selectCheckoutMethod() {
        return ORDER_METHOD_SELECTION_VIEW;
    }

    @RequestMapping("/checkout")
    public String initCheckout(Model model, @ModelAttribute("cart") ShoppingCart cart) {
        model.addAttribute("newCheckoutForm", new NewCheckoutForm());
        model.addAttribute("cart", cart);
        return ORDER_CHECKOUT_VIEW;
    }

    @RequestMapping(value = "/checkout", method = POST)
    public String doCheckout(@Valid @ModelAttribute NewCheckoutForm newCheckoutForm, BindingResult result,
                             @ModelAttribute("cart") ShoppingCart cart, SessionStatus status, RedirectAttributes ra) {
        if (result.hasErrors())
            return ORDER_CHECKOUT_VIEW;
        Customer customer = newCheckoutForm.getCustomer();
        Address address = newCheckoutForm.getAddress();
        address.setCustomer(customer);
        customer.add(address);
        customerRepository.save(customer);
        Order order = new Order(customer, address, cart.getContents());
        service.placeOrder(order);
        status.setComplete();
        ra.addFlashAttribute("message", "Order was placed successfully! Order ID: " + order.getId());
        return "redirect:/products";
    }

    @RequestMapping("/accountCheckout")
    public String initAuthenticatedCheckout(@AuthenticationPrincipal User authUser, Model model,
                                            @ModelAttribute("cart") ShoppingCart cart) {
        if (authUser == null)
            return "redirect:/order/checkout";
        Customer customer = ((UserService.User) authUser).getAccount().getCustomer();
        model.addAttribute("customer", customer);
        model.addAttribute("address", new Address());
        model.addAttribute("cart", cart);
        return ORDER_CHECKOUT_VIEW;
    }

    @RequestMapping(value = "/accountCheckout", method = POST)
    public String doCheckoutAuthenticated(@Valid @ModelAttribute("address") Address address,
                                          BindingResult result,
                                          @AuthenticationPrincipal User authUser,
                                          @ModelAttribute("cart") ShoppingCart cart, SessionStatus status,
                                          RedirectAttributes ra, Model model) {
        if (result.hasErrors()) {
            if (authUser != null) {
                model.addAttribute("customer", ((UserService.User) authUser).getAccount().getCustomer());
            }
            return authUser == null ? "redirect:/products" : ORDER_CHECKOUT_VIEW;
        }
        if (authUser == null) {
            return "redirect:/order/checkout";
        }
        Customer customer = ((UserService.User) authUser).getAccount().getCustomer();
        if (address.isNew()) {
            address.setCustomer(customer);
            address = addressRepository.save(address);
            customer.add(address);
            customerRepository.save(customer);
        }
        Order order = new Order(customer, address, cart.getContents());
        service.placeOrder(order);
        status.setComplete();
        ra.addFlashAttribute("message", "Order was placed successfully! Order ID: " + order.getId());
        return "redirect:/products";
    }

    @RequestMapping("/findOrder")
    public String checkOrderStatus() {
        return ORDER_FIND_VIEW;
    }

    @RequestMapping(value = "/showOrderStatus")
    public String findOrder(@RequestParam("orderId") Long orderId,
                            @RequestParam("firstname") String firstname,
                            @RequestParam("lastname") String lastname,
                            Model model) {
        Order order = service.findOrder(orderId, firstname, lastname);
        if (order != null) model.addAttribute("orders", Arrays.asList(order));
        return ORDER_LIST_VIEW;
    }

    @RequestMapping("/history")
    public String showOrderHistory(@AuthenticationPrincipal User activeUser, Model model) {
        List<Order> orders = service.findByCustomerEmail(activeUser == null ? null : activeUser.getUsername());
        model.addAttribute("orders", orders);
        return ORDER_LIST_VIEW;
    }
}