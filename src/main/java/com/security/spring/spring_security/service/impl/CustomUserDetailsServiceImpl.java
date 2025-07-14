package com.security.spring.spring_security.service.impl;

import com.security.spring.spring_security.entity.CustomUserDetails;
import com.security.spring.spring_security.repo.CustomUserDetailsRepo;
import com.security.spring.spring_security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CustomUserDetailsRepo userDetailsRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails customUserDetails = userDetailsRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not present in database."));
        return customUserDetails;
    }

    @Override
    public void createUser(UserDetails user) {
        CustomUserDetails customUserDetails= (CustomUserDetails) user;
        if(checkFormat(user.getUsername()) || checkFormat(user.getPassword())){
            throw new BadCredentialsException("Username and Password cannot have : in it");
        }
        Optional<CustomUserDetails> userDetails = userDetailsRepo.findByUsername(customUserDetails.getUsername());
        if(userDetails.isPresent()){
            throw new RuntimeException("User with same userName already present");
        }
        customUserDetails.setPassword(passwordEncoder.encode(customUserDetails.getPassword()));
        userDetailsRepo.save(customUserDetails);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    private boolean checkFormat(String value){
        return value.contains(":");
    }
}
