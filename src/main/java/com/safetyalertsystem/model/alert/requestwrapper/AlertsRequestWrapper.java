package com.safetyalertsystem.model.alert.requestwrapper;

import java.util.List;

import com.safetyalertsystem.model.alert.requestdto.AlertRequestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class AlertsRequestWrapper {

    @NotEmpty(message = "Alerts list cannot be empty")
    @Valid
    private List<AlertRequestDTO> alerts;

    public List<AlertRequestDTO> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<AlertRequestDTO> alerts) {
        this.alerts = alerts;
    }
}
