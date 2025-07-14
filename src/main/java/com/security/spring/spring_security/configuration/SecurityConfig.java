package com.security.spring.spring_security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth->
                auth.requestMatchers("/user/login/**").permitAll().anyRequest().authenticated()).httpBasic(httpSecurityHttpBasicConfigurer -> {});
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
