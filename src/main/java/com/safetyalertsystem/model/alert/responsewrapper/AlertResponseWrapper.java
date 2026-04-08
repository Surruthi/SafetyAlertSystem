package com.safetyalertsystem.model.alert.responsewrapper;

import java.util.List;

import com.safetyalertsystem.model.alert.responsedto.AlertResponseDTO;

import jakarta.validation.Valid;

public class AlertResponseWrapper {

    @Valid
    private List<AlertResponseDTO> alerts;

    public AlertResponseWrapper() {
    }

    public AlertResponseWrapper(List<AlertResponseDTO> alerts) {
        this.alerts = alerts;
    }

    public List<AlertResponseDTO> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<AlertResponseDTO> alerts) {
        this.alerts = alerts;
    }
}
