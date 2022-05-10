package com.velialiev.service;

import com.velialiev.dto.AuthenticationResponseDto;
import com.velialiev.dto.LoginRequestDto;
import com.velialiev.dto.RefreshAccessTokenRequestDto;
import com.velialiev.dto.RegisterRequestDto;
import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.model.NotificationEmail;
import com.velialiev.model.UserEntity;
import com.velialiev.model.VerificationTokenEntity;
import com.velialiev.repository.UserRepository;
import com.velialiev.repository.VerificationTokenRepository;
import com.velialiev.security.JWT;
import lombok.AllArgsConstructor;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JWT jwt;


    @Transactional
    public void signup(RegisterRequestDto registerRequestDto){

        UserEntity userEntity = UserEntity.builder()
                .username(registerRequestDto.getUsername())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .createdDate(Instant.now())
                .isEnabled(false)
                .build();

        userRepository.save(userEntity);

        String token = generateVerificationToken(userEntity);
        mailService.sendMail(new NotificationEmail("Please activate your account",
                        userEntity.getEmail(), "Thank you for signing up to Reddit Clone, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));

    }
    @Transactional(readOnly = true)
    public UserEntity getCurrentUser(){
         Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getSubject())
                .orElseThrow(()->new SpringRedditException("No user with such username"));
    }


    @Transactional
    public String generateVerificationToken(UserEntity userEntity){
        String token = UUID.randomUUID().toString();

        VerificationTokenEntity verificationTokenEntity = VerificationTokenEntity.builder()
                .token(token)
                .userEntity(userEntity)
                .expiryDate(Instant.now().plusSeconds(600))
                .build();

        verificationTokenRepository.save(verificationTokenEntity);
        return token;
    }

    @Transactional
    public String verifyAccount(String token) {

        VerificationTokenEntity verificationTokenEntity = verificationTokenRepository.findByToken(token)
                .orElseThrow(()-> new SpringRedditException("Invalid Token"));

        Instant expirationDate = verificationTokenEntity.getExpiryDate();
        if (expirationDate.compareTo(Instant.now()) < 0)
            return "Verification Token Expired";

        enableUser(verificationTokenEntity);
        verificationTokenRepository.deleteById(verificationTokenEntity.getVerificationId());
        return "Activation Completed. You can now login with your username and password";
    }

    @Transactional
    public void enableUser(VerificationTokenEntity verificationTokenEntity){
        UserEntity userEntity = userRepository.findByUsername(verificationTokenEntity.getUserEntity().getUsername())
                .orElseThrow(()-> new SpringRedditException("User "+ verificationTokenEntity.getUserEntity().getUsername() + " not found"));

        userEntity.setEnabled(true);
        userRepository.save(userEntity);
    }

    public AuthenticationResponseDto login(LoginRequestDto loginRequestDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        // You can look up this context for authentication object to check if the user is logged in or not.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwt.generateAccessToken(authentication);
        String refreshToken = jwt.generateRefreshToken().getToken();
        String username = loginRequestDto.getUsername();

        return buildAuthenticationResponse(accessToken, refreshToken, username);
    }

    public AuthenticationResponseDto refreshAccessToken(RefreshAccessTokenRequestDto refreshAccessTokenRequestDto) {
        jwt.validateRefreshToken(refreshAccessTokenRequestDto.getRefreshToken());

        String accessToken = jwt.generateAccessTokenWithUserName(refreshAccessTokenRequestDto.getUsername());
        String refreshToken = refreshAccessTokenRequestDto.getRefreshToken();
        String username = refreshAccessTokenRequestDto.getUsername();

        return buildAuthenticationResponse(accessToken, refreshToken, username);
    }

    public void logout(String refreshToken) {
        jwt.deleteRefreshToken(refreshToken);
    }

    public AuthenticationResponseDto buildAuthenticationResponse(String accessToken, String refreshToken, String username){
        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresAt(Instant.now().plusMillis(jwt.getJwtExpirationInMillis()))
                .username(username)
                .build();
    }
}
