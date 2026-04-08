package com.safetyalertsystem.model.user.requestdto;

public class MedicalRecordRequestDTO {

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
