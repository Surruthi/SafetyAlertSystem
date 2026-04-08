package com.safetyalertsystem.model.user.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PhoneNumberRequestDTO {
    
    @NotBlank
    @Pattern(regexp = "^\\+[1-9][0-9]{1,3}[0-9]{4,14}$", message = "Phone number must have valid digits")
    private String number;

    private Boolean isPrimary = false;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}
