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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerRequestDto.getUsername());
        userEntity.setEmail(registerRequestDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        userEntity.setCreatedDate(Instant.now());
        userEntity.setEnabled(false);

        userRepository.save(userEntity);

        String token = generateVerificationToken(userEntity);
        mailService.sendMail(new NotificationEmail("Please activate your account",
                        userEntity.getEmail(), "Thank you for signing up to Reddit Clone, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));

    }

    public UserEntity getCurrentUser(){
         User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(user.getUsername())
                .orElseThrow(()->new SpringRedditException("No user with such username"));
    }



    private String generateVerificationToken(UserEntity userEntity){
        String token = UUID.randomUUID().toString();

        VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity();
        verificationTokenEntity.setToken(token);
        verificationTokenEntity.setUserEntity(userEntity);

        verificationTokenRepository.save(verificationTokenEntity);
        return token;
    }

    public void verifyAccount(String token) {

        VerificationTokenEntity verificationTokenEntity = verificationTokenRepository.findByToken(token)
                .orElseThrow(()-> new SpringRedditException("Invalid Token"));

        fetchUserAndEnable(verificationTokenEntity);
    }

    @Transactional
    public void fetchUserAndEnable(VerificationTokenEntity verificationTokenEntity){
        UserEntity userEntity = userRepository.findByUsername(verificationTokenEntity.getUserEntity().getUsername())
                .orElseThrow(()-> new SpringRedditException("User "+ verificationTokenEntity.getUserEntity().getUsername() + " not found"));

        userEntity.setEnabled(true);
        userRepository.save(userEntity);
    }

    public AuthenticationResponseDto login(LoginRequestDto loginRequestDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        // You can look up this context for authentication object to check if the user is logged in or not.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwt.generateToken(authentication);
        return AuthenticationResponseDto.builder()
                .accessToken(token)
                .refreshToken(jwt.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwt.getJwtExpirationInMillis()))
                .username(loginRequestDto.getUsername())
                .build();
    }

    public AuthenticationResponseDto refreshToken(RefreshAccessTokenRequestDto refreshAccessTokenRequestDto) {
        jwt.validateRefreshToken(refreshAccessTokenRequestDto.getRefreshToken());
        String accessToken = jwt.generateTokenWithUserName(refreshAccessTokenRequestDto.getUsername());
        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshAccessTokenRequestDto.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwt.getJwtExpirationInMillis()))
                .username(refreshAccessTokenRequestDto.getUsername())
                .build();
    }

    public void logout(String refreshToken) {
        jwt.deleteRefreshToken(refreshToken);
    }
}
