package com.security.spring.spring_security.configuration;

import com.security.spring.spring_security.service.impl.CustomAuthenticationFilter;
import com.security.spring.spring_security.service.impl.CustomAuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

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
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler= new CsrfTokenRequestAttributeHandler();
        http.cors(corsConfig->corsConfig.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration= new CorsConfiguration();
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.addAllowedHeader("*");
                corsConfiguration.setAllowedMethods(List.of("*"));
                corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                corsConfiguration.setMaxAge(3600L);
                return corsConfiguration;
            }
        }));
        http.addFilterAfter(new CSRFFilter(), BasicAuthenticationFilter.class);
        http.csrf(csrfConfig->csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())).authorizeHttpRequests(auth->
                auth.requestMatchers("/user/login/**").permitAll().anyRequest().authenticated()).httpBasic(httpSecurityHttpBasicConfigurer -> {});
        http.addFilterBefore(customAuthenticationFilter, BasicAuthenticationFilter.class);
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
