package com.safetyalertsystem.model.alert.responsedto;

public class AlertResponseDTO {

    private Long id;
    private String status;

    public AlertResponseDTO() {
    }

    public AlertResponseDTO(Long id, String status) {
        this.id = id;
        this.status = status;
    }

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
}
