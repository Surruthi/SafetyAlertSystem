package com.safetyalertsystem.util.call.providers;

import java.net.URI;
import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.safetyalertsystem.GlobalExceptionHandler;
import com.safetyalertsystem.configs.call.TwilioConfig;

import com.twilio.Twilio;
import com.twilio.http.HttpMethod;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Call;

@Component
public class TCallProvider implements CallProvider {
    
    private final TwilioConfig config;
    
    public TCallProvider(TwilioConfig config) {
        this.config = config;
    }

    @Override
    public String makeCall(String phoneNumber){

        try
        {
            String webhookUrl = "https://nongenerative-suably-marybeth.ngrok-free.dev/safetyalertsystem/voice/start";
            String statusWebHookUrl = "https://nongenerative-suably-marybeth.ngrok-free.dev/safetyalertsystem/voice/status";

            Twilio.init(config.getAccountSid(), config.getAuthToken());

            Call call = Call.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(config.getPhoneNumber()),
                // Need to replace the URL with the actual endpoint that will handle the twilio call logic.
                new java.net.URI(webhookUrl))
            .setStatusCallback(new URI(statusWebHookUrl))
            .setStatusCallbackMethod(HttpMethod.POST)
            .setStatusCallbackEvent(
                    Arrays.asList(
                    "completed",
                          "busy",
                          "failed",
                          "no-answer",
                          "canceled"
                    )
            ).create();

            return call.getSid();
        }
        catch(Exception e) 
        {
            GlobalExceptionHandler.handleException(e);
            return null;
        }
    } 

    // private Call waitForCallCompletion(String callSid, int timeoutSeconds) throws InterruptedException
    // {
    //     int elapsedSeconds = 0;

    //     while (elapsedSeconds < timeoutSeconds) 
    //     {
    //         Call call = Call.fetcher(callSid).fetch();
    //         String status = call.getStatus().name().toString().toLowerCase();

    //         if ( status.equals("completed")
    //             || status.equals("busy") 
    //             || status.equals("failed") 
    //             || status.equals("no-answer") 
    //             || status.equals("canceled"))
    //         {
    //             return call;
    //         }

    //         Thread.sleep(2000);

    //         elapsedSeconds += 2;
    //     }

    //     throw new RuntimeException("Call did not placed within the expected time frame.");
    // }

    // private CallResult generateCallResult(Call call)
    // {
    //     CallResult result = new CallResult();

    //     result.setSid(call.getSid());
    //     result.setQueueTime(call.getQueueTime());
    //     result.setStartTime(call.getStartTime());
    //     result.setEndTime(call.getEndTime());
    //     result.setDuration(call.getDuration());
    //     result.setStatus(CallStatus.valueOf(call.getStatus().toString().toUpperCase()));
        
    //     return result;
    // }
}  