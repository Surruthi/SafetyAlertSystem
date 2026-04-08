package com.safetyalertsystem.repository.alert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safetyalertsystem.entity.alert.Alert;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
}
