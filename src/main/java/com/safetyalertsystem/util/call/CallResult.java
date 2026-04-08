package com.safetyalertsystem.util.call;

import com.safetyalertsystem.enums.call.CallStatus;
import java.time.ZonedDateTime;

/** Remove This Class - Not Used - Changed to Event Driven Architecture Flow */
public class CallResult {
    
    private String sid;
    private String queueTime;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private String duration;
    private CallStatus status;
    
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getQueueTime() {
        return queueTime;
    }

    public void setQueueTime(String queueTime) {
        this.queueTime = queueTime;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public CallStatus getStatus() {
        return status;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }
}
