package com.safetyalertsystem.entity.alert;

import java.time.Instant;

import com.safetyalertsystem.enums.alert.AlertTriggeredFrom;
import com.safetyalertsystem.entity.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Instant triggeredAt;

    @Enumerated(EnumType.STRING)
    private AlertTriggeredFrom triggeredMode;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "alert_location_id", referencedColumnName = "id")
    private AlertLocation location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public AlertLocation getLocation() {
        return location;
    }

    public void setLocation(AlertLocation location) {
        this.location = location;
    }
}
