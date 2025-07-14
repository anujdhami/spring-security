package com.security.spring.spring_security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class UserDetailsDTO {
    @NotBlank
    @Size(min = 4,message = "UserName should be of more than 4 characters")
    private String username;
    @NotBlank
    @Size(min = 8,message = "Password should be of more than 4 characters")
    private String password;
    List<SimpleGrantedAuthority> list= new ArrayList<>();
}
