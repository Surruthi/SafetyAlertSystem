package com.safetyalertsystem.model.alert.requestdto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class AlertUserDTO {

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format")
    private String phoneNumber;

    private long userId;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
