package com.practice.shop.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Order(2)
public class WebSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
}