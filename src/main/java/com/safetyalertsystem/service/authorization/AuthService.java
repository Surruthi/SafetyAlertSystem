package com.safetyalertsystem.service.authorization;

import org.checkerframework.checker.units.qual.t;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.safetyalertsystem.entity.user.User;
import com.safetyalertsystem.event.authentication.SendOtpEvent;
import com.safetyalertsystem.service.sms.SmsServiceValid;
import com.safetyalertsystem.service.authentication.OtpCacheService;
import com.safetyalertsystem.service.authentication.OtpService;
import com.safetyalertsystem.service.user.UserService;
import com.safetyalertsystem.model.authorization.AuthResponseDTO;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    OtpService otpService;

    @Autowired
    OtpCacheService otpCacheService;

    @Autowired
    SmsServiceValid smsService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public AuthResponseDTO verifyOtp(String phoneNumber, String otpInput) 
    {
        boolean isValid = otpService.verifyOtp(phoneNumber, otpInput);
        
        if (!isValid) 
        {
            throw new RuntimeException("OTP verification failed");
        }

        userService.updateUserByPhoneNumber(phoneNumber);
        otpCacheService.remove(phoneNumber);

        String accessToken = jwtService.generateAccessToken(phoneNumber);
        String refreshToken = jwtService.generateRefreshToken(phoneNumber);

        AuthResponseDTO response = new AuthResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setMessage("User verified and logged in");

        return response;
    }

    public void sendOtp(String phoneNumber) 
    {
        User user = userService.getUserByPhoneNumber(phoneNumber);
        eventPublisher.publishEvent(new SendOtpEvent(this, user));
    }
}
