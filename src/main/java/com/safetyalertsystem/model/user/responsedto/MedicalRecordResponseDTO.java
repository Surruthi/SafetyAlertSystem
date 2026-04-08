package com.safetyalertsystem.model.user.responsedto;

public class MedicalRecordResponseDTO {

    private Long id;

    private String allergies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
}