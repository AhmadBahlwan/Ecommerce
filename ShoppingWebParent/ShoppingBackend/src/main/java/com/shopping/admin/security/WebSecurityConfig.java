package com.shopping.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new ShopingUserDetailsService();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(form-> form.loginPage("/login").usernameParameter("email").permitAll());

        http.authorizeHttpRequests(auth-> {
           auth.requestMatchers("/images/**", "/js/**", "/webjars/**").permitAll();
           auth.requestMatchers("/users/**").hasAuthority("Admin");
           auth.requestMatchers("/categories/**").hasAnyAuthority("Admin", "Editor");
           auth.requestMatchers("/brands/**").hasAnyAuthority("Admin", "Editor");
           auth.requestMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor");
           auth.requestMatchers("/products/edit/**", "/products/save", "/products/check_unique").hasAnyAuthority("Admin", "Editor", "Salesperson");
           auth.requestMatchers("/products", "/products/detail/**", "/products/page/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper");
           auth.requestMatchers("/products/**").hasAnyAuthority("Admin", "Editor");
           auth.requestMatchers("/questions/**").hasAnyAuthority("Admin", "Assistant");
           auth.requestMatchers("/reviews/**").hasAnyAuthority("Admin", "Assistant");
           auth.requestMatchers("/customers/**").hasAnyAuthority("Admin", "SalesPerson");
           auth.requestMatchers("/shipping/**").hasAnyAuthority("Admin", "SalesPerson");
           auth.requestMatchers("/orders/**").hasAnyAuthority("Admin", "SalesPerson", "Shipper");
           auth.requestMatchers("/reports/**").hasAnyAuthority("Admin", "SalesPerson");
           auth.requestMatchers("/articles/**").hasAnyAuthority("Admin", "Editor");
           auth.requestMatchers("/menus/**").hasAnyAuthority("Admin", "Editor");
           auth.requestMatchers("/setting/**").hasAnyAuthority("Admin");
           auth.anyRequest().authenticated();

        });

        http.logout(LogoutConfigurer::permitAll);
        http.rememberMe(rememberMe->
                rememberMe.userDetailsService(userDetailsService())
                        .key("AbcDefgHijklmnOpqrs_1234567890")
                        .tokenValiditySeconds(7 * 24 * 60 * 60));
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
