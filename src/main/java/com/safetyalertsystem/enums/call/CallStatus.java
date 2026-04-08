package com.safetyalertsystem.enums.call;

public enum CallStatus {
    INITIATED("initiated"),
    QUEUED("queued"),
    RINGING("ringing"),
    IN_PROGRESS("in-progress"),
    COMPLETED("completed"),
    BUSY("busy"),
    FAILED("failed"),
    NO_ANSWER("no-answer"),
    CANCELED("canceled");

    private final String displayName;

    CallStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getStatus() {
        return displayName;
    }
}
