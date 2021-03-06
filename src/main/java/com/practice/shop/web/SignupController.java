package com.practice.shop.web;

import com.practice.shop.domain.Address;
import com.practice.shop.domain.Customer;
import com.practice.shop.domain.account.Account;
import com.practice.shop.domain.account.AccountRepository;
import com.practice.shop.domain.account.UserService;
import com.practice.shop.signup.SignupForm;
import com.practice.shop.support.AjaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@SessionAttributes("cart")
public class SignupController {

    public static final String SIGN_UP_VIEW = "signup/signup";

    private final AccountRepository accountRepository;
    private final UserService userService;

    @Autowired
    public SignupController(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    @RequestMapping(value = "signup")
    public String signup(Model model,
                         @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
        model.addAttribute("signupForm", new SignupForm());
        if (AjaxUtils.isAjaxRequest(requestedWith)) {
            return SIGN_UP_VIEW.concat(" :: signupForm");
        }
        return SIGN_UP_VIEW;
    }

    @RequestMapping(value = "signup", method = POST)
    public String signup(@Valid @ModelAttribute SignupForm signupForm, BindingResult result) {
        if (result.hasErrors()) {
            return SIGN_UP_VIEW;
        }
        Address address = signupForm.getAddress();
        Customer customer = signupForm.getCustomer();
        address.setCustomer(customer);
        customer.add(address);
        Account account = accountRepository.save(signupForm.createAccount());
        userService.signin(account);
        return "redirect:/";
    }
}
