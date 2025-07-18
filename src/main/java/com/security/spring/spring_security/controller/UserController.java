package com.security.spring.spring_security.controller;

import com.security.spring.spring_security.entity.CustomUserDetails;
import com.security.spring.spring_security.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/login")
public class UserController {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @PostMapping("/create-user")
    public String createUser(@RequestBody @Valid CustomUserDetails customUserDetails){
        customUserDetailsService.createUser(customUserDetails);
        return "User created successfully";
    }

    @GetMapping("/cors-testing")
    public String testCorsSettings(){
        return "Tested cors setting";
    }
}
