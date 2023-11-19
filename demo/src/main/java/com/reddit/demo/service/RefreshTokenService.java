package com.reddit.demo.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.reddit.demo.exception.SpringRedditExpection;
import com.reddit.demo.model.RefreshToken;
import com.reddit.demo.repository.RefreshTokenRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenrepository;
    @Transactional
  public RefreshToken generateRefreshToken(){
     RefreshToken refreshToken = new RefreshToken();
     refreshToken.setToken(UUID.randomUUID().toString());
     refreshToken.setCreatedDate(Instant.now());
     return refreshTokenrepository.save(refreshToken);
  }
  @Transactional
   public void validateRefreshToken(String token){
   refreshTokenrepository.findByToken(token)
    .orElseThrow(() -> new SpringRedditExpection("token not found") );
   }
   @Transactional
   public void deleteRefreshToken(String token)
   {
    refreshTokenrepository.deleteByToken(token);
   }
}
