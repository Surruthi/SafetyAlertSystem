package com.safetyalertsystem.event.call;

import com.safetyalertsystem.entity.call.CallAttempt;

public class CallStatusEvent {

    private final CallAttempt attempt;

    public CallStatusEvent(CallAttempt attempt) {
        this.attempt = attempt;
    }

    public CallAttempt getAttempt() {
        return attempt;
    }
}