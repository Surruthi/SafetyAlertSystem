package com.safetyalertsystem.repository.authentication;

import org.springframework.data.jpa.repository.JpaRepository;

import com.safetyalertsystem.entity.authentication.Otp;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> 
{
    Optional<Otp> findTopByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(String phoneNumber);
    Optional<Otp> findByMessageSid(String messageSid);
    void deleteByUsedTrueOrExpiresAtBefore(LocalDateTime now);
}