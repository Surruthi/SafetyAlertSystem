package com.safetyalertsystem.enums.sms;

public enum DeliveryStatus {
    QUEUED("queued"),
    SENDING("sending"),
    SENT("sent"),
    FAILED("failed"),
    DELIVERED("delivered"),
    UNDELIVERED("undelivered"),
    RECEIVING("receiving"),
    RECEIVED("received"),
    ACCEPTED("accepted"),
    SCHEDULED("scheduled"),
    READ("read"),
    PARTIALLY_DELIVERED("partially_delivered"),
    CANCELED("canceled");

    private final String status;

    DeliveryStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
