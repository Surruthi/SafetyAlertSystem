package com.safetyalertsystem.service.call;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.safetyalertsystem.entity.call.CallAttempt;
import com.safetyalertsystem.enums.call.CallStatus;
import com.safetyalertsystem.event.call.CallStatusEvent;
import com.safetyalertsystem.repository.call.CallAttemptRepository;
import com.twilio.http.HttpMethod;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Say;

@Service
public class TwilioVoiceService 
{
    
    private final CallAttemptRepository callAttemptRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TwilioVoiceService(CallAttemptRepository callAttemptRepository, ApplicationEventPublisher eventPublisher) 
    {
        this.callAttemptRepository = callAttemptRepository;
        this.eventPublisher = eventPublisher;
    }
    
    public String constructSystemResponse(String systemResponse) 
    {
        System.out.println("*** : " + systemResponse);

        String webhookUrl = "https://nongenerative-suably-marybeth.ngrok-free.dev/safetyalertsystem/voice/process-speech";
        Gather gather = new Gather.Builder()
            .inputs(Arrays.asList(Gather.Input.SPEECH))
            .action(webhookUrl)
            .method(HttpMethod.POST)
            .timeout(5)
            .speechTimeout("auto")
            .bargeIn(true)
            .say(new Say.Builder(systemResponse)
                    .voice(Say.Voice.ALICE)
                    .build())
            .build();

        VoiceResponse response = new VoiceResponse.Builder()
                .gather(gather)
                .build();

        return response.toXml();
    }


    public String generateResponse(String speech, String callSid) 
    {
        return "You said: " + speech;
    }
    
    public void handleCallStatusCallback(String callSid, String status) 
    {
        CallAttempt attempt = callAttemptRepository.findByCallSid(callSid)
                .orElseThrow(() -> new RuntimeException("Call not found with SID: " + callSid));

        attempt.setStatus(mapTwilioStatusToCallStatus(status));
        attempt.setUpdatedAt(LocalDateTime.now());

        Call call = Call.fetcher(attempt.getCallSid()).fetch();
        attempt.setDuration(call.getDuration()); 
        attempt.setStartTime(call.getStartTime());  
        attempt.setEndTime(call.getEndTime());

        callAttemptRepository.save(attempt);
        eventPublisher.publishEvent(new CallStatusEvent(attempt));
    }

    
    private CallStatus mapTwilioStatusToCallStatus(String twilioStatus)
     {
        if (twilioStatus == null) 
        {
            return CallStatus.FAILED;
        }

        return switch (twilioStatus.toLowerCase()) 
        {
            case "queued" -> CallStatus.QUEUED;
            case "ringing" -> CallStatus.RINGING;
            case "in-progress" -> CallStatus.IN_PROGRESS;
            case "completed" -> CallStatus.COMPLETED;
            case "busy" -> CallStatus.BUSY;
            case "failed" -> CallStatus.FAILED;
            case "no-answer" -> CallStatus.NO_ANSWER;
            case "canceled" -> CallStatus.CANCELED;
            default -> CallStatus.FAILED;
        };
    }
}
