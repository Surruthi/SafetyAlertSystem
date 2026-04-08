package com.safetyalertsystem.service.sms;

import com.safetyalertsystem.configs.call.TwilioConfig;
import com.safetyalertsystem.entity.authentication.Otp;
import com.safetyalertsystem.service.authentication.OtpCacheService;
import com.safetyalertsystem.service.authentication.OtpService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceValid 
{
    @Autowired
    private TwilioConfig twilioConfig;

    @Autowired
    private OtpService otpService;

    @Autowired
    private OtpCacheService cacheService;

    public Message sendOtp(String toPhoneNumber, String otp) 
    {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());

        return Message.creator(
            new PhoneNumber(toPhoneNumber),
            new PhoneNumber(twilioConfig.getPhoneNumber()),
            "Your OTP code is: " + otp + " (valid for 5 minutes)")
            .setStatusCallback("https://yourapp.com/api/sms/status")
            .create();
    }

    public void handleMessageStatusCallback(String messageSid, String messageStatus) 
    {
        Otp otp = otpService.getOtpByMessageSid(messageSid);

        if (messageStatus.equalsIgnoreCase("failed") ||
            messageStatus.equalsIgnoreCase("undelivered") ||
            messageStatus.equalsIgnoreCase("canceled")) 
        {
            if (otp.getRetryCount() < 3) 
            {
                String otpPlain = cacheService.get(otp.getPhoneNumber());

                Message message = sendOtp(otp.getPhoneNumber(), otpPlain);

                otpService.incrementRetry(otp, message.getSid());
                return;
            }
        }

        otpService.updateDeliveryStatus(messageSid, messageStatus.toUpperCase());
    }
}
