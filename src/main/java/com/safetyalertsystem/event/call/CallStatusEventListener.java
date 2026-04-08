package com.safetyalertsystem.event.call;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import com.safetyalertsystem.entity.call.CallAttempt;
import com.safetyalertsystem.entity.user.PhoneNumber;
import com.safetyalertsystem.enums.call.CallStatus;
import com.safetyalertsystem.repository.call.CallAttemptRepository;
import com.safetyalertsystem.repository.user.UserRepository;
import com.safetyalertsystem.util.call.CallManager;


@Component
public class CallStatusEventListener {

    @Autowired
    private CallAttemptRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CallManager callManager;

    private static final int MAX_RETRIES = 3;

    @EventListener
    public void handleCallStatus(CallStatusEvent event) {

        CallAttempt attempt = event.getAttempt();

        if (attempt.getStatus() == CallStatus.IN_PROGRESS) {
            attempt.setCompleted(true);
            repository.save(attempt);
            return;
        }

        if (attempt.getAttemptCount() + 1 < MAX_RETRIES) {

            CallAttempt newAttempt = createNewAttemptForRetry(attempt);

            repository.save(newAttempt);

            callManager.makeCall(newAttempt);
            return;
        }

        attempt.setCompleted(true);
        repository.save(attempt);

        List<CallAttempt> attempts = repository.findByAlertIdOrderByIdAsc(attempt.getAlertId());

        List<String> allNumbers = getPhoneNumbersForAlert(attempt.getAlertId());

        Optional<String> nextNumber = findNextNumber(allNumbers, attempts);

        if (nextNumber.isPresent()) 
        {
            CallAttempt newAttempt = new CallAttempt();
            newAttempt.setAlertId(attempt.getAlertId());
            newAttempt.setUserId(attempt.getUserId());
            newAttempt.setPhoneNumber(nextNumber.get());
            newAttempt.setAttemptCount(0);
            newAttempt.setStatus(CallStatus.QUEUED);
            newAttempt.setCompleted(false);
            newAttempt.setCreatedAt(LocalDateTime.now());

            repository.save(newAttempt);

            callManager.makeCall(newAttempt);
        } 
        else 
        {
            // Escalate to risk situation
        }
    }

    private Optional<String> findNextNumber(List<String> allNumbers, List<CallAttempt> attempts) 
    {
        Set<String> attemptedNumbers = attempts.stream()
                .map(CallAttempt::getPhoneNumber)
                .collect(Collectors.toSet());

        return allNumbers.stream()
                .filter(number -> !attemptedNumbers.contains(number))
                .findFirst();
    }

    private List<String> getPhoneNumbersForAlert(Long userId) 
    {
        return userRepository.findById(userId)
                .map(user -> user.getPhoneNumbers().stream()
                        .map(PhoneNumber::getNumber)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    private CallAttempt createNewAttemptForRetry(CallAttempt previousAttempt) 
    {   
        CallAttempt newAttempt = new CallAttempt();
        newAttempt.setAlertId(previousAttempt.getAlertId());
        newAttempt.setUserId(previousAttempt.getUserId());
        newAttempt.setPhoneNumber(previousAttempt.getPhoneNumber());
        newAttempt.setAttemptCount(previousAttempt.getAttemptCount() + 1);
        newAttempt.setStatus(CallStatus.QUEUED);
        newAttempt.setCompleted(false);
        newAttempt.setCreatedAt(LocalDateTime.now());

        return newAttempt;
    }
}