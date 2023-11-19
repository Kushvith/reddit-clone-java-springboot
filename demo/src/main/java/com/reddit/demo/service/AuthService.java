package com.reddit.demo.service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.reddit.demo.DTO.AuthenticateResponse;
import com.reddit.demo.DTO.LoginRequest;
import com.reddit.demo.DTO.NotificationEmail;
import com.reddit.demo.DTO.RefreshTokenRequest;
import com.reddit.demo.DTO.RegisterRequest;
import com.reddit.demo.DTO.AuthenticateResponse.AuthenticateResponseBuilder;
import com.reddit.demo.exception.SpringRedditExpection;
import com.reddit.demo.model.User;
import com.reddit.demo.model.VerificationToken;
import com.reddit.demo.repository.UserRepository;
import com.reddit.demo.repository.VerificationRepository;
import com.reddit.demo.security.jwtProvider;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@AllArgsConstructor
  @Slf4j
public class AuthService {
   private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationRepository verificationRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final jwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    
    @Transactional
  
    public void signup(RegisterRequest registerRequest){
      final Optional<User> exuser = userRepository.findByusername(registerRequest.getUsername());
      if(!exuser.isPresent()){
           User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);
       String token =  generateVerificationToken(user);
       log.info("token: "+token);
       NotificationEmail notificationEmail = new NotificationEmail();
       notificationEmail.setBody(token);
        mailService.sendMail(new NotificationEmail("Please Activate your account",
                user.getEmail(),
                  "Thank you for signin up to spring Reddit\n please click on the below url to activate accoumt \n http://localhost:8080/api/auth/accountVerification"+token 
                 
        ));
      }
      else{
        throw new SpringRedditExpection("User already exits");
      }
                                                                                      

    }

    public void verifyAccount(String token){
     Optional<VerificationToken> verificationToken =  verificationRepository.findByToken(token);
      verificationToken.orElseThrow(() -> new SpringRedditExpection("invalid token"));
      fetchUserEnable(verificationToken.get());
    }

    private void fetchUserEnable(VerificationToken verificationToken) {
      String email = verificationToken.getUser().getUsername();
      User user = userRepository.findByusername(email).orElseThrow(() -> new SpringRedditExpection("username not found"));
      user.setEnabled(true);
      userRepository.save(user);
    }

    private String generateVerificationToken(User user) {
       String token =  UUID.randomUUID().toString();
       VerificationToken verificationToken = new VerificationToken();
       verificationToken.setToken(token);
       verificationToken.setUser(user);
       verificationRepository.save(verificationToken);
       return token;
    }
    public AuthenticateResponse login(LoginRequest loginRequest)
    {
     Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String token = jwtProvider.generateToken(loginRequest.getEmail());
      return  AuthenticateResponse.builder()
      .authenticationToken(token)
      .refreshToken(refreshTokenService.generateRefreshToken().getToken())
      .username(loginRequest.getEmail())
      .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationMillis())).build();
    }
    public User getCurrentUser(){
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
     
      return userRepository.findByusername(auth.getName()).orElseThrow(()-> new SpringRedditExpection("User Not found"));
    }
    public AuthenticateResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
      refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
      String token = jwtProvider.generateToken(refreshTokenRequest.getUsername());
      return AuthenticateResponse.builder()
      .authenticationToken(token)
      .refreshToken(refreshTokenRequest.getRefreshToken())
      .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationMillis()))
      .username(refreshTokenRequest.getUsername())
      .build();
    }
}
