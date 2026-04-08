package com.safetyalertsystem.model.user.requestdto;

import com.safetyalertsystem.enums.user.AddressType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddressRequestDTO {

    private Long id;

    private AddressType addressType;

    @NotNull(message = "Address line 1 cannot be null")
    @Size(max = 100, message = "Address line 1 cannot exceed 100 characters")
    private String addressLine1;

    @NotNull(message = "Address line 2 cannot be null")
    @Size(max = 100, message = "Address line 2 cannot exceed 100 characters")
    private String addressLine2;

    @NotNull(message = "City cannot be null")
    private String city;

    @NotNull(message = "County cannot be null")
    private String county;

    @NotNull(message = "Eir code cannot be null")
    private String eircode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getEircode() {
        return eircode;
    }

    public void setEircode(String eircode) {
        this.eircode = eircode;
    }
}
