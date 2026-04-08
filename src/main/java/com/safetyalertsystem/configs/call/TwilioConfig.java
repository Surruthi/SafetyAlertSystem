package com.safetyalertsystem.configs.call;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "twilio")
public class TwilioConfig {

    private String accountSid;
    private String authToken;
    private String phoneNumber;
    private String apiKeySid;
    private String apiKeySecret;

    public String getAccountSid() {
        return accountSid;
    }   
    
    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getApiKeySid() {
        return apiKeySid;
    }

    public void setApiKeySid(String apiKeySid) {
        this.apiKeySid = apiKeySid;
    }

    public String getApiKeySecret() {
        return apiKeySecret;
    }


    public void setApiKeySecret(String apiKeySecret) {
        this.apiKeySecret = apiKeySecret;
    }
    
}
