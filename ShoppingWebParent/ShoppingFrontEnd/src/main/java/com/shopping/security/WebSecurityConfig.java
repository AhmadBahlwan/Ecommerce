package com.shopping.security;

import com.shopping.security.oauth.CustomerOAuth2UserService;
import com.shopping.security.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired private CustomerOAuth2UserService oAuth2UserService;
    @Autowired private OAuth2LoginSuccessHandler oauth2LoginHandler;
    @Autowired
    private DatabaseLoginSuccessHandler databaseLoginHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth->{
            auth.requestMatchers("/images/**", "/js/**", "/webjars/**").permitAll();
            auth.requestMatchers("/account_details", "/update_account_details", "/cart", "/address_book/**", "/checkout", "/place_order").authenticated().anyRequest().permitAll();
        });

        http.formLogin(login-> login.loginPage("/login")
                .usernameParameter("email")
                .successHandler(databaseLoginHandler)
                .permitAll());

        http.oauth2Login(oauth -> {
            oauth.loginPage("/login").userInfoEndpoint(service -> service.userService(oAuth2UserService));
            oauth.successHandler(oauth2LoginHandler);
        });

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        http.logout(LogoutConfigurer::permitAll);
        http.rememberMe(rem -> rem.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
                .tokenValiditySeconds(14 * 24 * 60 * 60));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}