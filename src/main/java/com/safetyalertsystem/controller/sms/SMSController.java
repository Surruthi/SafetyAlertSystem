package com.safetyalertsystem.controller.sms;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetyalertsystem.service.sms.SmsServiceValid;

@RestController
@RequestMapping("/sms")
public class SMSController 
{

    private final SmsServiceValid smsService;

    public SMSController(SmsServiceValid smsService) 
    {
        this.smsService = smsService;
    }

    @PostMapping(value = "/status", produces = "application/xml")
    public void callStatusCallback(
            @RequestParam("MessageSid") String messageSid,
            @RequestParam("MessageStatus") String messageStatus) 
    {
        smsService.handleMessageStatusCallback(messageSid, messageStatus);
    }
}