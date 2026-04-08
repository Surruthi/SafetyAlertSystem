package com.safetyalertsystem.enums.user;

public enum AddressType {
    
    HOME("Home"),
    WORK("Work"),
    OTHER("Other");

    private final String displayName;

    AddressType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
