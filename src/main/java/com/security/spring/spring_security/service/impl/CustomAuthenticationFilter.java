package com.security.spring.spring_security.service.impl;

import com.security.spring.spring_security.configuration.SecurityConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Service
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    AuthenticationManager authenticationManager;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationToken = request.getHeader("Authorization");
        if(authorizationToken.isBlank()){
            throw new BadCredentialsException("Empty username and password");
        }
        authorizationToken= authorizationToken.substring("Basic ".length());
        CustomAuthenticationToken authenticationToken = getToken(authorizationToken);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(authenticate != null){
            System.out.println("From custom filter");
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }
        filterChain.doFilter(request,response);
    }

    private CustomAuthenticationToken getToken(String authorizationToken){
        if(authorizationToken.isBlank()){
            throw new BadCredentialsException("Empty username and password");
        }
        byte[] decode = Base64.getDecoder().decode(authorizationToken);
        String decodedString= new String(decode);
        decodedString= decodedString.trim();
        String username= decodedString.substring(0,decodedString.lastIndexOf(":"));
        String password= decodedString.substring(decodedString.indexOf(":")+1);
        if(username.isBlank() || password.isBlank()){
            throw new BadCredentialsException("UserName and password cannot be empty");
        }
        return new CustomAuthenticationToken(username,password);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return SecurityConfig.PUBLIC_URLS.stream()
                .anyMatch(publicUrl -> pathMatcher.match(publicUrl, path));
    }
}
