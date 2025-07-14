package com.security.spring.spring_security.configuration;

import com.security.spring.spring_security.service.impl.CustomAuthenticationFilter;
import com.security.spring.spring_security.service.impl.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    public static final List<String> PUBLIC_URLS = List.of(
            "/user/login/**");

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(CustomAuthenticationProvider customAuthenticationProvider){
        return new ProviderManager(customAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationFilter customAuthenticationFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth->
                auth.requestMatchers("/user/login/**").permitAll().anyRequest().authenticated()).httpBasic(httpSecurityHttpBasicConfigurer -> {});
        http.addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return  http.build();
    }

    /*
    @Bean
    public UserDetailsService userDetailsService(){
        PasswordEncoder passwordEncoder= passwordEncoder();
        UserDetails user = User.withUsername("anuj").password(passwordEncoder.encode("123")).authorities("read").build();
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(user);
        return userDetailsManager;
    }
*/
}
