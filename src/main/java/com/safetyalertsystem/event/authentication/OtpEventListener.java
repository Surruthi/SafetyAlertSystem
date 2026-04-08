package com.safetyalertsystem.event.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.safetyalertsystem.entity.authentication.Otp;
import com.safetyalertsystem.entity.user.PhoneNumber;
import com.safetyalertsystem.entity.user.User;
import com.safetyalertsystem.enums.sms.DeliveryStatus;
import com.safetyalertsystem.service.sms.SmsServiceValid;
import com.safetyalertsystem.service.authentication.OtpCacheService;
import com.safetyalertsystem.service.authentication.OtpService;
import com.twilio.rest.api.v2010.account.Message;

@Component
public class OtpEventListener {

    @Autowired
    private SmsServiceValid smsService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private OtpCacheService otpCacheService;

    @EventListener
    public void handleUserCreated(SendOtpEvent event) 
    {
        User user = event.getUser();

        String primaryNumber = user.getPhoneNumbers().stream()
                .filter(PhoneNumber::getIsPrimary)
                .findFirst()
                .map(PhoneNumber::getNumber)
                .orElseThrow(() -> new RuntimeException("No primary phone number found"));

        String otp = otpService.generateOtp(primaryNumber);

        otpCacheService.store(primaryNumber, otp);

        Message message = smsService.sendOtp(primaryNumber, otp);

        Otp otpEntity = otpService.findTopByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(primaryNumber);

        otpEntity.setMessageSid(message.getSid());
        otpEntity.setDeliveryStatus(DeliveryStatus.QUEUED);

        otpService.saveOtp(otpEntity);
    }
}