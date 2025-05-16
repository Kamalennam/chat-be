package com.chat.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chat.models.LoginRequestDto;
import com.chat.models.LoginResponseDto;
import com.chat.models.OtpVerificationRequest;
import com.chat.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class Users {

    private final AuthService authService;

    public Users(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.loginWithMobile(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
        boolean isValid = authService.verifyOtp(request);
        if (isValid) {
            return ResponseEntity.ok("OTP verification successful.");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP or mobile number.");
        }
    }

}
