package com.practice.shop.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes("cart")
public class SigninController {

    private static final String INDEX_VIEW = "/";
    private static final String SIGNIN_VIEW = "signin/signin";

    @RequestMapping(value = "signin")
    public String signin() {
        return SIGNIN_VIEW;
    }

    @RequestMapping(value = "signinError")
    public String signinError(Model model) {
        model.addAttribute("loginError", true);
        return SIGNIN_VIEW;
    }

    @RequestMapping(value = "logout")
    public String logout(HttpServletRequest request) {
        Assert.notNull(request, "HttpServletRequest required");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return "redirect:" + INDEX_VIEW;
    }
}
