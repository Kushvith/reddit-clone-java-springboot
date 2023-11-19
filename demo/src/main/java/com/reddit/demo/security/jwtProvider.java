package com.reddit.demo.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.reddit.demo.exception.SpringRedditExpection;


import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Service
@AllArgsConstructor
@NoArgsConstructor
public class jwtProvider {
    private KeyStore keyStore;
    @Value("${jwt.expiration.time}")
    private Long epirationTimeInMilli;
    @PostConstruct
    public void init(){
       try {
         keyStore = KeyStore.getInstance("JKS");
         InputStream resourceStream = getClass().getResourceAsStream("/springblog.jks");
         keyStore.load(resourceStream, "123456".toCharArray());
       } catch (KeyStoreException|CertificateException|NoSuchAlgorithmException|IOException e) {
        throw new SpringRedditExpection("Expection Occured while loading keystore");
       }

    }
    public Long getJwtExpirationMillis(){
        return epirationTimeInMilli;
    }
    public String generateToken(String Username){
        return Jwts.builder()
                .subject(Username)
                .signWith(getPrivateKey())
                .issuedAt(Date.from(Instant.now()))
                // .expiration(Date.from(Instant.now().plusMillis(epirationTimeInMilli)))
                .compact();
    }
    private PrivateKey getPrivateKey(){
        try {
            return (PrivateKey) keyStore.getKey("springblog", "123456".toCharArray());
            
        } catch (Exception e) {
            throw new SpringRedditExpection("Expection Occured while retrieving public key from keystore");
        }
    }
    public boolean validateToken(String token){
        Jwts.parser().verifyWith(getPublicKey()).build().parseSignedClaims(token);
        return true;
    }
    public String getUsernameFromToken(String token){
        return  Jwts.parser().verifyWith(getPublicKey()).build()
        .parseSignedClaims(token).getPayload().getSubject();
    }

    private PublicKey getPublicKey() {
       
            try {
                return keyStore.getCertificate("springblog").getPublicKey();
            } catch (KeyStoreException e) {
               throw new SpringRedditExpection("Expection occured while validating the token");
            }
    
    }
}
