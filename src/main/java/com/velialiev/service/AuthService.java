package com.velialiev.service;

import com.velialiev.dto.RegisterRequest;
import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.model.NotificationEmail;
import com.velialiev.model.User;
import com.velialiev.model.VerificationToken;
import com.velialiev.repository.UserRepository;
import com.velialiev.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;


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

    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedDate(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please activate your account",
                        user.getEmail(), "Thank you for signing up to Reddit Clone, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));

    }

    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(()-> new SpringRedditException("Invalid Token"));

        fetchUserAndEnable(verificationToken);
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken){
        User user = userRepository.findByUsername(verificationToken.getUser().getUsername())
                .orElseThrow(()-> new SpringRedditException("User "+ verificationToken.getUser().getUsername() + " not found"));

        user.setEnabled(true);
        userRepository.save(user);
    }
}
