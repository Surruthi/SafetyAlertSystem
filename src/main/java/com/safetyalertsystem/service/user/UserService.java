package com.safetyalertsystem.service.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.safetyalertsystem.entity.user.Address;
import com.safetyalertsystem.entity.user.EmergencyContact;
import com.safetyalertsystem.entity.user.MedicalRecord;
import com.safetyalertsystem.entity.user.PhoneNumber;
import com.safetyalertsystem.entity.user.User;
import com.safetyalertsystem.event.authentication.SendOtpEvent;
import com.safetyalertsystem.repository.user.UserRepository;
import com.safetyalertsystem.model.user.requestdto.EmergencyContactRequestDTO;
import com.safetyalertsystem.model.user.requestdto.UserRequestDTO;
import com.safetyalertsystem.model.user.responsedto.AddressResponseDTO;
import com.safetyalertsystem.model.user.responsedto.EmergencyContactResponseDTO;
import com.safetyalertsystem.model.user.responsedto.MedicalRecordResponseDTO;
import com.safetyalertsystem.model.user.responsedto.PhoneNumberResponseDTO;
import com.safetyalertsystem.model.user.responsedto.UserResponseDTO;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        try 
        {
            User user = mapToEntity(userRequestDTO);

            User savedUser = userRepository.save(user);

            eventPublisher.publishEvent(new SendOtpEvent(this, savedUser));

            return mapToUserIdResponse(savedUser);
        } 
        catch (Exception ex) 
        {
            throw new RuntimeException("Failed to create user: " + ex.getMessage(), ex);
        }
    }

    @Transactional
    public UserResponseDTO patchUser(UserRequestDTO userRequestDTO) {

        User existingUser = getUserById(userRequestDTO.getId());

        // Update basic fields
        if (userRequestDTO.getFirstName() != null) {
            existingUser.setFirstName(userRequestDTO.getFirstName());
        }
        if (userRequestDTO.getLastName() != null) {
            existingUser.setLastName(userRequestDTO.getLastName());
        }
        if (userRequestDTO.getEmail() != null) {
            existingUser.setEmail(userRequestDTO.getEmail());
        }
        if (userRequestDTO.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(userRequestDTO.getDateOfBirth());
        }
        if (userRequestDTO.getGender() != null) {
            existingUser.setGender(userRequestDTO.getGender());
        }
        if (userRequestDTO.getBloodGroup() != null) {
            existingUser.setBloodGroup(userRequestDTO.getBloodGroup());
        }

        // Update Phone Numbers
        if (userRequestDTO.getPhoneNumbers() != null && !userRequestDTO.getPhoneNumbers().isEmpty()) 
        {
            List<PhoneNumber> newPhoneNumbers = userRequestDTO.getPhoneNumbers()
                    .stream()
                    .map(dto -> {
                        PhoneNumber pn = new PhoneNumber();
                        pn.setNumber(dto.getNumber());
                        pn.setIsPrimary(dto.getIsPrimary());
                        pn.setUser(existingUser);
                        return pn;
                    })
                    .collect(Collectors.toList());

            validatePrimaryPhoneNumber(newPhoneNumbers);
            existingUser.getPhoneNumbers().clear();
            existingUser.getPhoneNumbers().addAll(newPhoneNumbers);
        }

        // Update Addresses
        if (userRequestDTO.getAddresses() != null && !userRequestDTO.getAddresses().isEmpty()) 
        {
            List<Address> newAddresses = userRequestDTO.getAddresses().stream()
                    .map(dto -> {
                        Address addr = new Address();
                        addr.setAddressType(dto.getAddressType());
                        addr.setAddressLine1(dto.getAddressLine1());
                        addr.setAddressLine2(dto.getAddressLine2());
                        addr.setCity(dto.getCity());
                        addr.setCounty(dto.getCounty());
                        addr.setEircode(dto.getEircode());
                        addr.setUser(existingUser);
                        return addr;
                    })
                    .collect(Collectors.toList());

            existingUser.getAddresses().clear();
            existingUser.getAddresses().addAll(newAddresses);
        }

        // Update Emergency Contacts
        if (userRequestDTO.getEmergencyContacts() != null && !userRequestDTO.getEmergencyContacts().isEmpty()) 
        {
            validateEmerygencyContacts(userRequestDTO.getEmergencyContacts());
            List<EmergencyContact> newContacts = userRequestDTO.getEmergencyContacts()
                    .stream()
                    .map(c -> {
                        EmergencyContact ec = new EmergencyContact();
                        ec.setFirstName(c.getFirstName());
                        ec.setLastName(c.getLastName());
                        ec.setPhoneNumber(c.getPhoneNumber());
                        ec.setRelationship(c.getRelationship());
                        ec.setUser(existingUser);
                        return ec;
                    })
                    .toList();

            existingUser.getEmergencyContacts().clear();
            existingUser.getEmergencyContacts().addAll(newContacts);
        }

        // Update Medical Records
        if (userRequestDTO.getMedicalRecords() != null && !userRequestDTO.getMedicalRecords().isEmpty()) {
           
            List<MedicalRecord> newMedicalRecords = userRequestDTO.getMedicalRecords()
                    .stream()
                    .map(m -> {
                        MedicalRecord mr = new MedicalRecord();
                        mr.setAllergies(m.getAllergies());
                        mr.setUser(existingUser);
                        return mr;
                    })
                    .toList();

            existingUser.getMedicalRecords().clear();
            existingUser.getMedicalRecords().addAll(newMedicalRecords);
        }

        try 
        {
            User updatedUser = userRepository.save(existingUser);
            return mapToResponse(updatedUser);
        } 
        catch (Exception ex) 
        {
            throw new RuntimeException("Failed to update user: " + ex.getMessage(), ex);
        }
    }

    public UserResponseDTO getUserByIdResponse(Long id) {

        User user = getUserById(id);
        return mapToResponse(user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        try 
        {
            User user = getUserById(id);
            userRepository.delete(user);
        }
        catch (Exception ex) 
        {
            throw new RuntimeException("Failed to delete user with id: " + id + ". " + ex.getMessage(), ex);
        }
    }

    public User getUserById(Long id) {
        try 
        {
            return userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        } 
        catch (Exception ex) 
        {
            throw new RuntimeException("Database error while fetching user with id: " + id + ". " + ex.getMessage(), ex);
        }
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        try 
        {
            return userRepository.findByPhoneNumbers_Number(phoneNumber)
                    .orElseThrow(() -> new RuntimeException("User not found with phone number: " + phoneNumber));
        } 
        catch (Exception ex) 
        {
            throw new RuntimeException("Database error while fetching user with phone number: " + phoneNumber + ". " + ex.getMessage(), ex);
        }
    }

    private User mapToEntity(UserRequestDTO userRequestDTO) {

        User user = new User();
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setEmail(userRequestDTO.getEmail());
        user.setDateOfBirth(userRequestDTO.getDateOfBirth());
        user.setGender(userRequestDTO.getGender());
        user.setBloodGroup(userRequestDTO.getBloodGroup());
        user.setVerified(false);

        //Phone Numbers
        List<PhoneNumber> phoneNumbers = userRequestDTO.getPhoneNumbers()
                .stream()
                .map(dto -> {
                    PhoneNumber pn = new PhoneNumber();
                    pn.setNumber(dto.getNumber());
                    pn.setIsPrimary(dto.getIsPrimary());
                    pn.setUser(user);
                    return pn;
                })
                .collect(Collectors.toList()); 

        validatePrimaryPhoneNumber(phoneNumbers);
        user.setPhoneNumbers(phoneNumbers);

        //addresses
        List<Address> addressList = userRequestDTO.getAddresses().stream()
        .map(dto -> {
            Address addr = new Address();
            addr.setAddressType(dto.getAddressType());
            addr.setAddressLine1(dto.getAddressLine1());
            addr.setAddressLine2(dto.getAddressLine2());
            addr.setCity(dto.getCity());
            addr.setCounty(dto.getCounty());
            addr.setEircode(dto.getEircode());
            addr.setUser(user);
            return addr;
        })
        .collect(Collectors.toList());

        user.setAddresses(addressList);

        // Emergency Contacts
        validateEmerygencyContacts(userRequestDTO.getEmergencyContacts());
        List<EmergencyContact> contacts = userRequestDTO.getEmergencyContacts()
                .stream()
                .map(c -> {
                    EmergencyContact ec = new EmergencyContact();
                    ec.setFirstName(c.getFirstName());
                    ec.setLastName(c.getLastName());
                    ec.setPhoneNumber(c.getPhoneNumber());
                    ec.setUser(user);
                    ec.setRelationship(c.getRelationship());
                    return ec;
                })
                .toList();

        user.setEmergencyContacts(contacts);

        // Medical Records
        List<MedicalRecord> medicalRecords = userRequestDTO.getMedicalRecords()
                .stream()
                .map(m -> {
                    MedicalRecord mr = new MedicalRecord();
                    mr.setAllergies(m.getAllergies());
                    mr.setUser(user);
                    return mr;
                })
                .toList();

        user.setMedicalRecords(medicalRecords);

        return user;
    }

    private void validatePrimaryPhoneNumber(List<PhoneNumber> phoneNumbers) 
    {
        long primaryCount = phoneNumbers.stream().filter(PhoneNumber::getIsPrimary).count();
        if (primaryCount == 0) 
        {
            phoneNumbers.get(0).setIsPrimary(true);
        } 
        else if (primaryCount > 1) 
        {
            throw new RuntimeException("Only one primary phone number is allowed.");
        }
    }

    private void validateEmerygencyContacts(List<EmergencyContactRequestDTO> emergencyContacts) 
    {
        Set<String> phoneNumbers = new HashSet<>();
        for (EmergencyContactRequestDTO contact : emergencyContacts) {
            if (!phoneNumbers.add(contact.getPhoneNumber())) {
                throw new RuntimeException("Duplicate phone number found in emergency contacts: " + contact.getPhoneNumber());
            }
        }
    }

    private UserResponseDTO mapToUserIdResponse(User user) 
    {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        return response;
    }

    private UserResponseDTO mapToResponse(User user) {

        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setGender(user.getGender());
        response.setBloodGroup(user.getBloodGroup());

        //Phone Numbers
        List<PhoneNumberResponseDTO> phoneNumberDTOs = user.getPhoneNumbers()
                .stream()
                .map(pn -> {
                    PhoneNumberResponseDTO pnDTO = new PhoneNumberResponseDTO();
                    pnDTO.setId(pn.getId());
                    pnDTO.setNumber(pn.getNumber());
                    pnDTO.setIsPrimary(pn.getIsPrimary());
                    return pnDTO;
                })
                .collect(Collectors.toList());
        response.setPhoneNumbers(phoneNumberDTOs);

        //addresses
        List<AddressResponseDTO> addressDTOs = new ArrayList<>();

        if(user.getAddresses() != null) {
            for (Address addr : user.getAddresses()) {
                AddressResponseDTO addressResponseDTO = new AddressResponseDTO();
                addressResponseDTO.setId(addr.getId());
                addressResponseDTO.setAddressType(addr.getAddressType());
                addressResponseDTO.setAddressLine1(addr.getAddressLine1());
                addressResponseDTO.setAddressLine2(addr.getAddressLine2());
                addressResponseDTO.setCity(addr.getCity());
                addressResponseDTO.setCounty(addr.getCounty());
                addressResponseDTO.setEircode(addr.getEircode());
                addressDTOs.add(addressResponseDTO);
            }
            response.setAddresses(addressDTOs);
        }

        List<EmergencyContactResponseDTO> emergencyContacts = new ArrayList<>();

        if (user.getEmergencyContacts() != null) {
            for (EmergencyContact contact : user.getEmergencyContacts()) {
                EmergencyContactResponseDTO contactResponse = new EmergencyContactResponseDTO();
                contactResponse.setId(contact.getId());
                contactResponse.setFirstName(contact.getFirstName());
                contactResponse.setLastName(contact.getLastName());
                contactResponse.setPhoneNumber(contact.getPhoneNumber());
                emergencyContacts.add(contactResponse);
            }
            response.setEmergencyContacts(emergencyContacts);
        }

        List<MedicalRecordResponseDTO> medicalRecords = new ArrayList<>();

        // Medical Records
        List<MedicalRecord> medicalRecordsList = user.getMedicalRecords();
        if (medicalRecordsList != null) {
            for (MedicalRecord mr : medicalRecordsList) {
                MedicalRecordResponseDTO mrResponse = new MedicalRecordResponseDTO();
                mrResponse.setId(mr.getId());
                mrResponse.setAllergies(mr.getAllergies());
                medicalRecords.add(mrResponse);
            }
            response.setMedicalRecords(medicalRecords);
        }

        return response;
    }

    public UserResponseDTO login(String phoneNumber) 
    {
        User user = getUserByPhoneNumber(phoneNumber);

        eventPublisher.publishEvent(new SendOtpEvent(this, user));

        return mapToUserIdResponse(user);
    }

    public void updateUserByPhoneNumber(String phoneNumber) 
    {
         User user = getUserByPhoneNumber(phoneNumber);

         user.setVerified(true);
         userRepository.save(user);
    }
}
