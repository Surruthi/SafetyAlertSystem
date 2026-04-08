package com.safetyalertsystem.controller.authorization;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetyalertsystem.model.authorization.AuthResponseDTO;
import com.safetyalertsystem.model.authentication.OtpRequestDTO;
import com.safetyalertsystem.service.authorization.AuthService;
import com.safetyalertsystem.service.authorization.JWTService;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController 
{
    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam String refreshToken) {

        if (!jwtService.isValid(refreshToken)) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }

        String phone = jwtService.extractUsername(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(phone);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    @PostMapping("/verify-otp")
    public AuthResponseDTO verifyOtp(@RequestBody OtpRequestDTO request) 
    {
       return authService.verifyOtp(
                request.getPhoneNumber(),
                request.getOtp()
        );
    }

    @PostMapping("/send-otp")
    public void sendOtp(@RequestBody OtpRequestDTO request) 
    {
        authService.sendOtp(request.getPhoneNumber());
    }
}
