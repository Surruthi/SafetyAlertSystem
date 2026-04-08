package com.safetyalertsystem.service.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.safetyalertsystem.entity.authentication.Otp;
import com.safetyalertsystem.enums.sms.DeliveryStatus;
import com.safetyalertsystem.repository.authentication.OtpRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    private static final int EXPIRY_MINUTES = 5;
    private static final SecureRandom random = new SecureRandom();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    public String generateOtp(String phoneNumber) 
    {
        String otp = String.format("%06d", random.nextInt(1_000_000));

        Otp otpEntity = new Otp();
        otpEntity.setPhoneNumber(phoneNumber);
        otpEntity.setOtpHash(encoder.encode(otp));
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(EXPIRY_MINUTES));
        otpEntity.setAttempts(1);
        otpEntity.setUsed(false);

        otpRepository.save(otpEntity);

        return otp;
    }

    public Otp getOtpByMessageSid(String messageSid) {
        return otpRepository.findByMessageSid(messageSid)
                .orElseThrow(() -> new RuntimeException("OTP not found"));
    }

    public void updateAfterSend(Long otpId, String messageSid) {
        Otp otp = otpRepository.findById(otpId).orElseThrow();
        otp.setMessageSid(messageSid);
        otp.setDeliveryStatus(DeliveryStatus.QUEUED);
        otpRepository.save(otp);
    }

    public void updateDeliveryStatus(String messageSid, String status) {
        Otp otp = getOtpByMessageSid(messageSid);
        otp.setDeliveryStatus(DeliveryStatus.valueOf(status.toUpperCase()));
        otpRepository.save(otp);
    }

    public void incrementRetry(Otp otp, String newMessageSid) {
        otp.setRetryCount(otp.getRetryCount() + 1);
        otp.setMessageSid(newMessageSid);
        otp.setDeliveryStatus(DeliveryStatus.QUEUED);
        otpRepository.save(otp);
    }

    public boolean verifyOtp(String phoneNumber, String otpInput) 
    {
        Otp otpEntity = otpRepository.findTopByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(phoneNumber)
                .orElseThrow(() -> new RuntimeException("OTP not found or already used"));


        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now()))
        {  
            throw new RuntimeException("OTP expired");
        }

        if (otpEntity.getAttempts() >= 5) 
        {
            throw new RuntimeException("Maximum attempts exceeded");
        }

        otpEntity.setAttempts(otpEntity.getAttempts() + 1);

        if (encoder.matches(otpInput, otpEntity.getOtpHash()))
        {
            otpEntity.setUsed(true);
            otpRepository.save(otpEntity);
            return true;
        } 
        else 
        {
            otpRepository.save(otpEntity);
            return false;
        }
    }
    
    public String regenerateOtp(String phoneNumber) 
    {
        Otp otpEntity = otpRepository.findTopByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(phoneNumber)
                .orElseThrow(() -> new RuntimeException("OTP not found or already used"));

        if (otpEntity.getExpiresAt().isAfter(LocalDateTime.now()))
        {  
            throw new RuntimeException("Use existing OTP before requesting a new one");
        }

        if (otpEntity.getAttempts() >= 3) 
        {
            throw new RuntimeException("Maximum OTP regeneration limit (3) exceeded for phone number: " + phoneNumber);
        }

        String otp = String.format("%06d", random.nextInt(1_000_000));
        String hashedOtp = BCrypt.hashpw(otp, BCrypt.gensalt());
    
        otpEntity.setOtpHash(hashedOtp);
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(EXPIRY_MINUTES));
        otpEntity.setAttempts(otpEntity.getAttempts() + 1);
        otpEntity.setUsed(false);

        otpRepository.save(otpEntity);

        return otp;
    }

    public Otp findTopByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(String phoneNumber) 
    {
        return otpRepository.findTopByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(phoneNumber)
                .orElseThrow(() -> new RuntimeException("OTP not found for phone number: " + phoneNumber));
    }
    
    public void saveOtp(Otp otpEntity) 
    {
        otpRepository.save(otpEntity);
    }

    @Scheduled(fixedRate = 600000) // every 10 minutes
    public void cleanupOtps() 
    {
        otpRepository.deleteByUsedTrueOrExpiresAtBefore(LocalDateTime.now());
    }
}
