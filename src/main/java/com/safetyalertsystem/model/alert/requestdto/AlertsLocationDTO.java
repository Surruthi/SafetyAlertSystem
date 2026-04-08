package com.safetyalertsystem.model.alert.requestdto;

import jakarta.validation.constraints.NotNull;

public class AlertsLocationDTO {

    @NotNull(message = "Latitude cannot be null")
    private double latitude;

    @NotNull(message = "Longitude cannot be null")
    private double longitude;

    private double accuracy;

    private String source;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
