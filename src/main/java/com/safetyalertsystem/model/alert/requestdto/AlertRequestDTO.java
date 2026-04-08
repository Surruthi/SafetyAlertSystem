package com.safetyalertsystem.model.alert.requestdto;

import java.time.Instant;

import com.safetyalertsystem.enums.alert.AlertTriggeredFrom;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class AlertRequestDTO {

    @Valid
    @NotNull(message = "User cannot be null")
    private AlertUserDTO user;

    @NotNull(message = "Triggered at cannot be null")
    private Instant triggeredAt;

    @NotNull(message = "Triggered mode cannot be null")
    private AlertTriggeredFrom triggeredMode;

    @Valid
    private AlertsLocationDTO location;

    public AlertUserDTO getUser() {
        return user;
    }

    public void setUser(AlertUserDTO user) {
        this.user = user;
    }

    public Instant getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(Instant triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    public AlertTriggeredFrom getTriggeredMode() {
        return triggeredMode;
    }

    public void setTriggeredMode(AlertTriggeredFrom triggeredMode) {
        this.triggeredMode = triggeredMode;
    }

    public AlertsLocationDTO getLocation() {
        return location;
    }
}
