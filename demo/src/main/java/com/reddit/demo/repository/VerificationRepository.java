package com.reddit.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reddit.demo.model.VerificationToken;

public interface VerificationRepository extends JpaRepository<VerificationToken,Long>{

    Optional<VerificationToken> findByToken(String token);
    
}
