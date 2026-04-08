package com.safetyalertsystem.model.user.responsewrapper;

import java.util.List;

import com.safetyalertsystem.model.user.responsedto.UserResponseDTO;

import jakarta.validation.Valid;

public class UserResponseWrapper {

    @Valid
    private List<UserResponseDTO> users;

    public UserResponseWrapper() {
    }

    public UserResponseWrapper(List<UserResponseDTO> users) {
        this.users = users;
    }

    public List<UserResponseDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponseDTO> users) {
        this.users = users;
    }
}
