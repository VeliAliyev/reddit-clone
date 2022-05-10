package com.velialiev.config;

import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.model.RefreshTokenEntity;
import com.velialiev.repository.JWTRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class JWT {

    private final JwtEncoder jwtEncoder;
    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;
    private final JWTRepository JWTRepository;

    public String generateAccessToken(Authentication authentication){

        User principal = (User) authentication.getPrincipal();
        return generateAccessTokenWithUserName(principal.getUsername());
    }

    public String generateAccessTokenWithUserName(String username){
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtExpirationInMillis))
                .subject(username)
                .claim("scope", "ROLE_USER")
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public RefreshTokenEntity generateRefreshToken(){

        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        return JWTRepository.save(refreshToken);
    }

    public void validateRefreshToken(String token){
        JWTRepository.findByToken(token).orElseThrow(()->new SpringRedditException("Invalid Refresh Token"));
    }

    public void deleteRefreshToken(String token){
        JWTRepository.deleteByToken(token);
    }

    public Long getJwtExpirationInMillis(){return this.jwtExpirationInMillis;}
}
