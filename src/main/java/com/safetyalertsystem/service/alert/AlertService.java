package com.safetyalertsystem.service.alert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safetyalertsystem.entity.alert.Alert;
import com.safetyalertsystem.entity.alert.AlertLocation;
import com.safetyalertsystem.repository.alert.AlertRepository;
import com.safetyalertsystem.model.alert.requestdto.AlertsLocationDTO;
import com.safetyalertsystem.model.alert.requestdto.AlertRequestDTO;
import com.safetyalertsystem.model.alert.requestdto.AlertUserDTO;
import com.safetyalertsystem.model.alert.responsedto.AlertResponseDTO;
import com.safetyalertsystem.entity.user.User;
import com.safetyalertsystem.event.call.AlertCreatedEvent;
import com.safetyalertsystem.service.user.UserService;

@Service
public class AlertService 
{

    private final AlertRepository alertRepository;
    private final UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public AlertService(AlertRepository alertRepository, UserService userService) 
    {
        this.alertRepository = alertRepository;
        this.userService = userService;
    }

    @Transactional
    public AlertResponseDTO createAlert(AlertRequestDTO alertRequestDTO) 
    {
        try
        {
            Alert alert = mapToEntity(alertRequestDTO);

            Alert savedAlert = alertRepository.save(alert);

            // AlertCreatedEvent event = new AlertCreatedEvent(
            //         savedAlert.getId(), savedAlert.getUser().getId(), savedAlert.getUser().getPhoneNumbers());

            // eventPublisher.publishEvent(event);

            return mapToResponse(savedAlert);
        } 
        catch (Exception ex) 
        {
            throw new RuntimeException("Failed to create alert: " + ex.getMessage(), ex);
        }
    }

    private Alert mapToEntity(AlertRequestDTO alertRequestDTO) 
    {
        Alert alert = new Alert();

        alert.setStatus("CREATED");
        alert.setUser(getUser(alertRequestDTO.getUser()));
        alert.setTriggeredAt(alertRequestDTO.getTriggeredAt());
        alert.setTriggeredMode(alertRequestDTO.getTriggeredMode());
        alert.setLocation(getLocation(alertRequestDTO.getLocation()));
        
        return alert;
    }

    private AlertLocation getLocation(AlertsLocationDTO locationDTO) 
    {
        if (locationDTO == null) 
        {

            // handle the case by getting the location from various source in separate
            // thread.

            return null;
        }

        AlertLocation alertLocation = new AlertLocation();

        alertLocation.setLatitude(locationDTO.getLatitude());
        alertLocation.setLongitude(locationDTO.getLongitude());
        alertLocation.setAccuracy(locationDTO.getAccuracy());
        alertLocation.setSource(locationDTO.getSource());

        return alertLocation;
    }

    private User getUser(AlertUserDTO alertUserDTO) 
    {
        try 
        {
            if (alertUserDTO.getUserId() != 0) 
            {
                return userService.getUserById(alertUserDTO.getUserId());
            }

            User user = userService.getUserByPhoneNumber(alertUserDTO.getPhoneNumber());

            return user;
        } 
        catch (Exception ex) 
        {
            throw new RuntimeException("Failed to fetch user for alert: " + ex.getMessage(), ex);
        }
    }

    private AlertResponseDTO mapToResponse(Alert alert) 
    {
        AlertResponseDTO alertResponseDTO = new AlertResponseDTO();

        alertResponseDTO.setId(alert.getId());
        alertResponseDTO.setStatus(alert.getStatus());

        return alertResponseDTO;
    }
}
