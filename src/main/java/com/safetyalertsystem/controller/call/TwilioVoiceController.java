package com.safetyalertsystem.controller.call;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetyalertsystem.service.call.TwilioVoiceService;

@RestController
@RequestMapping("/voice")
public class TwilioVoiceController 
{

    private final TwilioVoiceService twilioVoiceService;

    public TwilioVoiceController(TwilioVoiceService twilioVoiceService) 
    {
        this.twilioVoiceService = twilioVoiceService;
    }

    @PostMapping(value = "/start", produces = "application/xml")
    public String startVoice() 
    {
        return twilioVoiceService.constructSystemResponse("Hello, You can speak now.");
    }

    @PostMapping(value = "/process-speech", produces = "application/xml")
    public String processSpeech(
            @RequestParam(value = "SpeechResult", required = false) String speech,
            @RequestParam("CallSid") String callSid) 
    {

        if (speech == null || speech.trim().isEmpty()) 
        {
            speech = "Silence detected.";
        }

        String reply = twilioVoiceService.generateResponse(speech, callSid);
        return twilioVoiceService.constructSystemResponse(reply);
    }

    @PostMapping(value = "/status", produces = "application/xml")
    public void callStatusCallback(
            @RequestParam("CallSid") String callSid,
            @RequestParam("CallStatus") String status) 
    {
        twilioVoiceService.handleCallStatusCallback(callSid, status);
    }
}
