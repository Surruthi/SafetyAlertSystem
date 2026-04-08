package com.safetyalertsystem.event.call;

import java.util.List;

import com.safetyalertsystem.entity.user.PhoneNumber;

public class AlertCreatedEvent {

    private final Long alertId;
    private final Long userId;
    private final List<PhoneNumber> phoneNumbers;

    public AlertCreatedEvent(Long alertId, Long userId, List<PhoneNumber> phoneNumbers) {
        this.alertId = alertId;
        this.userId = userId;
        this.phoneNumbers = phoneNumbers;
    }

    public Long getAlertId() {
        return alertId;
    }

    public Long getUserId() {
        return userId;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }
}