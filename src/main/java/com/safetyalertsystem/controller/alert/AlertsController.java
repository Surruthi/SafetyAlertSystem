package com.safetyalertsystem.controller.alert;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safetyalertsystem.model.alert.requestdto.AlertRequestDTO;
import com.safetyalertsystem.model.alert.requestwrapper.AlertsRequestWrapper;
import com.safetyalertsystem.model.alert.responsedto.AlertResponseDTO;
import com.safetyalertsystem.model.alert.responsewrapper.AlertResponseWrapper;
import com.safetyalertsystem.service.alert.AlertService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/alerts")
public class AlertsController {

    private final AlertService alertService;

    public AlertsController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public ResponseEntity<AlertResponseWrapper> createAlert(
            @Valid @RequestBody AlertsRequestWrapper alertRequestWrapper) {

        List<AlertResponseDTO> alertResponseDTOs = new ArrayList<>();

        for (AlertRequestDTO alertRequestDTO : alertRequestWrapper.getAlerts()) {

            alertResponseDTOs.add(alertService.createAlert(alertRequestDTO));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AlertResponseWrapper(alertResponseDTOs));
    }
}
