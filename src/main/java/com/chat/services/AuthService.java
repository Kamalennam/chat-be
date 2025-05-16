package com.chat.services;

import org.springframework.stereotype.Service;

import com.chat.entity.User;
import com.chat.models.LoginRequestDto;
import com.chat.models.LoginResponseDto;
import com.chat.models.OtpVerificationRequest;
import com.chat.repositories.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final Random random = new Random();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponseDto loginWithMobile(LoginRequestDto request) {
        String otp = String.valueOf(1000 + random.nextInt(9000)); 
        Instant now = Instant.now();

        Optional<User> existingUser = userRepository.findByMobileNumber(request.getMobileNumber());

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setOtp(otp);
            user.setUpdatedAt(now);
            userRepository.save(user);
            return new LoginResponseDto("OTP updated for existing user", otp);
        } else {
            User newUser = new User();
            newUser.setMobileNumber(request.getMobileNumber());
            newUser.setOtp(otp);
            newUser.setCreatedAt(now);
            newUser.setUpdatedAt(now);
            userRepository.save(newUser);
            return new LoginResponseDto("New user created and OTP generated", otp);
        }
    }
    
    public boolean verifyOtp(OtpVerificationRequest request) {
        return userRepository.findByMobileNumber(request.getMobileNumber())
                .map(user -> user.getOtp().equals(request.getOtp()))
                .orElse(false);
    }

}
