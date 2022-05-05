package com.velialiev.service;

import com.velialiev.dto.AuthenticationResponse;
import com.velialiev.dto.LoginRequest;
import com.velialiev.dto.RegisterRequest;
import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.model.NotificationEmail;
import com.velialiev.model.UserEntity;
import com.velialiev.model.VerificationTokenEntity;
import com.velialiev.repository.UserRepository;
import com.velialiev.repository.VerificationTokenRepository;
import com.velialiev.security.JwtProvider;
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
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerRequest.getUsername());
        userEntity.setEmail(registerRequest.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
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
         UserEntity userEntity = userRepository.findByUsername(user.getUsername())
                 .orElseThrow(()->new SpringRedditException("No user with such username"));
         return userEntity;
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

    public AuthenticationResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        // You can look up this context for authentication object to check if the user is logged in or not.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return new AuthenticationResponse(token, loginRequest.getUsername());
    }

}
