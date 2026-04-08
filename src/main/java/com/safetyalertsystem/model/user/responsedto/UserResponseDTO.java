package com.safetyalertsystem.model.user.responsedto;

import java.time.LocalDate;
import java.util.List;

import com.safetyalertsystem.enums.user.BloodGroup;
import com.safetyalertsystem.enums.user.Gender;

public class UserResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private List<PhoneNumberResponseDTO> phoneNumbers;

    private Gender gender;

    private LocalDate dateOfBirth;

    private List<AddressResponseDTO> addresses;

    private List<EmergencyContactResponseDTO> emergencyContacts;

    private List<MedicalRecordResponseDTO> medicalRecords;

    private BloodGroup bloodGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<PhoneNumberResponseDTO> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumberResponseDTO> phoneNumbers) {
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

    public List<AddressResponseDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressResponseDTO> addresses) {
        this.addresses = addresses;
    }

    public List<EmergencyContactResponseDTO> getEmergencyContacts() {
        return emergencyContacts;
    }

    public void setEmergencyContacts(List<EmergencyContactResponseDTO> emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
    }

    public List<MedicalRecordResponseDTO> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecordResponseDTO> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
