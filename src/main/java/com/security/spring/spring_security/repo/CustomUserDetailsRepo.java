package com.security.spring.spring_security.repo;

import com.security.spring.spring_security.entity.CustomUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomUserDetailsRepo extends JpaRepository<CustomUserDetails,Long> {
    Optional<CustomUserDetails> findByUsername(String username);
}
