package com.safetyalertsystem.model.user.requestdto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.safetyalertsystem.enums.user.BloodGroup;
import com.safetyalertsystem.enums.user.Gender;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequestDTO {

    @NotNull(groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "First name cannot be null")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    @Pattern(regexp = "^[A-Za-z .'-]+$", message = "First name contains invalid characters")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    @Pattern(regexp = "^[A-Za-z .'-]+$", message = "Last name contains invalid characters")
    private String lastName;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Phone numbers cannot be null")
    @Size(min = 1, message = "At least one phone number required")
    private List<PhoneNumberRequestDTO> phoneNumbers;

    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @NotNull(message = "DOB cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Past(message = "DOB must be a past date")
    private LocalDate dateOfBirth;

    @NotNull(message = "Address cannot be null")
    @Size(min = 1, message = "At least one address required")
    @Valid
    private List<AddressRequestDTO> addresses;

    @NotNull(message = "Emergency contacts cannot be null")
    @Size(min = 2, message = "At least two emergency contact required")
    @Valid
    @JsonProperty("emergencyContacts")
    private List<EmergencyContactRequestDTO> emergencyContacts;

    @JsonProperty("medicalRecords")
    private List<MedicalRecordRequestDTO> medicalRecords;

    @NotNull(message = "Blood group cannot be null")
    private BloodGroup bloodGroup;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PhoneNumberRequestDTO> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumberRequestDTO> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<AddressRequestDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressRequestDTO> addresses) {
        this.addresses = addresses;
    }

    public List<EmergencyContactRequestDTO> getEmergencyContacts() {
        return emergencyContacts;
    }

    public void setEmergencyContacts(List<EmergencyContactRequestDTO> emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
    }

    public List<MedicalRecordRequestDTO> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecordRequestDTO> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}