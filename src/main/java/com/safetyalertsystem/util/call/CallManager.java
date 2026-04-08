package com.safetyalertsystem.util.call;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetyalertsystem.GlobalExceptionHandler;
import com.safetyalertsystem.entity.call.CallAttempt;
import com.safetyalertsystem.enums.call.CallStatus;
import com.safetyalertsystem.util.call.providers.CallProvider;
import com.safetyalertsystem.repository.call.CallAttemptRepository;

@Service
public class CallManager {
    
    @Autowired
    private CallProvider callProvider;

    @Autowired
    private CallAttemptRepository repository;

    // private static final int MAX_RETRIES = 3;

    public void makeCall(CallAttempt attempt) 
    {
        try 
        {
            String sid = callProvider.makeCall(attempt.getPhoneNumber());

            attempt.setCallSid(sid);
            attempt.setStatus(CallStatus.INITIATED);
            attempt.setUpdatedAt(LocalDateTime.now());

            repository.save(attempt);
        } 
        catch (Exception e) 
        {
            GlobalExceptionHandler.handleException(e);
        }
    }
    // public void makeCall(List<PhoneNumber> phoneNumbers, Long alertId) 
    // {
    //     boolean callSucceeded = false;

    //     // Try numbers in order (primary first)
    //     for (PhoneNumber phoneNumber : phoneNumbers) 
    //     {
    //         int attempts = 0;

    //         while (attempts < MAX_RETRIES) 
    //         {
    //             attempts++;

    //             try 
    //             {
    //                 CallResult result = callProvider.makeCall(getNumber(phoneNumber), alertId);

    //                 System.out.println("Attempt " + attempts + " for " + phoneNumber + " returned status: " + result.getStatus());

    //                 if (result.getStatus() == CallStatus.IN_PROGRESS || result.getStatus() == CallStatus.COMPLETED)
    //                 {
    //                     callSucceeded = true;
                        
    //                     break;
    //                 }
    //                 else 
    //                 {
    //                     Thread.sleep(1000);
    //                 }
    //             } 
    //             catch (Exception e) 
    //             {
    //                 GlobalExceptionHandler.handleException(e);
    //             }
    //         }

    //         if (callSucceeded) {
    //             break;
    //         }
    //     }

    //     if (!callSucceeded) {
    //         // Handle risk situation logic here
    //         System.err.println("All call attempts failed for alert ID: " + alertId);
    //     }
    // }

    // private String getNumber(PhoneNumber number) {
        
    //     return number.getCountryCode() + number.getNumber();
    // }
}