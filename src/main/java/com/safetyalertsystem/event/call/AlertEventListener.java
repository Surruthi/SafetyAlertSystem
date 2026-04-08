package com.safetyalertsystem.event.call;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.safetyalertsystem.entity.call.CallAttempt;
import com.safetyalertsystem.entity.user.PhoneNumber;
import com.safetyalertsystem.enums.call.CallStatus;
import com.safetyalertsystem.repository.call.CallAttemptRepository;
import com.safetyalertsystem.util.call.CallManager;

@Component
public class AlertEventListener {

    @Autowired
    private CallManager callManager;

    @Autowired
    private CallAttemptRepository callAttemptRepository;

    @Async
    @EventListener
    public void handleAlertCreated(AlertCreatedEvent event) {

        String primaryNumber = getPrimaryNumber(event.getPhoneNumbers());

        CallAttempt attempt = new CallAttempt();
        attempt.setAlertId(event.getAlertId());
        attempt.setUserId(event.getUserId());
        attempt.setPhoneNumber(primaryNumber);
        attempt.setAttemptCount(0);
        attempt.setStatus(CallStatus.QUEUED);
        attempt.setCompleted(false);
        attempt.setCreatedAt(LocalDateTime.now());
        

        callAttemptRepository.save(attempt);
        
        callManager.makeCall(attempt);
    }

    private String getPrimaryNumber(List<PhoneNumber> phoneNumbers) {
        return phoneNumbers.stream()
                .filter(PhoneNumber::getIsPrimary)
                .findFirst()
                .map(PhoneNumber::getNumber)
                .orElseThrow(() -> new RuntimeException("No primary phone number found for alert"));
    }
}