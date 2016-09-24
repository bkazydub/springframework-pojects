package com.practice.shop.domain.account;

import com.practice.shop.domain.Address;
import com.practice.shop.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.PostConstruct;

public class UserService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Populate active db with admin and customer.
     */
    @PostConstruct
    public void initialize() {
        Customer admin = new Customer("Firstname", "Lastname");
        admin.setPhoneNumber("0123456789");
        admin.setEmail("admin@ema.il");

        Account adminAccount = new Account(admin.getEmail(), "admin", "ROLE_ADMIN");
        adminAccount.setCustomer(admin);
        accountRepository.save(adminAccount);

        Customer customer = new Customer("John", "Doe");
        customer.setPhoneNumber("0986578722");
        customer.setEmail("my.email@gmail.com");

        Address customerAddress = new Address("Khreschatyk", "Kyiv", "Ukraine");
        customer.add(customerAddress);
        customerAddress.setCustomer(customer);

        Account customerAccount = new Account(customer.getEmail(), "password", "ROLE_USER");
        customerAccount.setCustomer(customer);
        accountRepository.save(customerAccount);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        return createUser(account);
    }

    public void signin(Account account) {
        SecurityContextHolder.getContext().setAuthentication(authenticate(account));
    }

    private Authentication authenticate(Account account) {
        return new UsernamePasswordAuthenticationToken(createUser(account), null, account.getAuthorities());
    }

    private User createUser(Account account) {
        return new User(account);
    }

    public static class User extends org.springframework.security.core.userdetails.User {

        private final Account account;

        public User(Account account) {
            super(account.getEmail(), account.getPassword(), account.getAuthorities());
            this.account = account;
        }

        public Account getAccount() {
            return account;
        }

        public boolean isAdmin() {
            return account.isAdmin();
        }
    }
}
