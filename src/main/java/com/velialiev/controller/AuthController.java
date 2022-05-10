package com.velialiev.controller;

import com.velialiev.dto.AuthenticationResponseDto;
import com.velialiev.dto.LoginRequestDto;
import com.velialiev.dto.RefreshAccessTokenRequestDto;
import com.velialiev.dto.RegisterRequestDto;


import com.velialiev.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequestDto registerRequestDto){
        authService.signup(registerRequestDto);
        return new ResponseEntity<>("Registration successful!", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable(name = "token") String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
    }

    @PostMapping("login")
    public AuthenticationResponseDto login(@RequestBody LoginRequestDto loginRequestDto){
        return authService.login(loginRequestDto);
    }

    @PostMapping("refresh/token")
    public AuthenticationResponseDto refreshToken(@Valid @RequestBody RefreshAccessTokenRequestDto refreshAccessTokenRequestDto){
        return authService.refreshAccessToken(refreshAccessTokenRequestDto);
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshAccessTokenRequestDto refreshAccessTokenRequestDto){
        authService.logout(refreshAccessTokenRequestDto.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Logged Out Successfully");
    }

}
