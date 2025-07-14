package com.security.spring.spring_security.service.impl;

import com.security.spring.spring_security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("From custom authentication provider");
        String userName= String.valueOf(authentication.getPrincipal());
        String password= String.valueOf(authentication.getCredentials());
        System.out.println("Username is: "+ userName);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        if(passwordEncoder.matches(password,userDetails.getPassword())){
            return new CustomAuthenticationToken(userDetails.getAuthorities(),userDetails,null);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
