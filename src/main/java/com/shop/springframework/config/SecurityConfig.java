
package com.shop.springframework.config;

import com.shop.springframework.domain.account.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // TODO updated 10/04/2016 22:00
        auth.userDetailsService(userService()).passwordEncoder(passwordEncoder());

        auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder())
                // the following query contains '1'; need to consider users.enabled
                .usersByUsernameQuery("select email, password, 1 from Account where email = ?")
                .authoritiesByUsernameQuery("select email, role from Account where email = ?");
    }

    @Bean
    public UserService userService() {
        return new UserService();
    }

    // TODO uncomment
    /*@Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices("remember-me-key", userService());
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/products/db/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                //.formLogin()
                .formLogin().loginPage("/signin").loginProcessingUrl("/signin")/*.failureUrl("/signin?error")*/
                .usernameParameter("j_username").passwordParameter("j_password")
                .and()
                //logout is missing logoutSuccessHandler and logoutHandler
                .logout().logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true)
                .and()
                .exceptionHandling().accessDeniedPage("/denied");
    }
}
