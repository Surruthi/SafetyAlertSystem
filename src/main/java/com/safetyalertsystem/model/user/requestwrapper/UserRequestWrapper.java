package com.safetyalertsystem.model.user.requestwrapper;

import java.util.List;

import com.safetyalertsystem.model.user.requestdto.UserRequestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class UserRequestWrapper {

    @NotEmpty(message = "Users list cannot be empty")
    @Valid
    private List<UserRequestDTO> users;

    public List<UserRequestDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserRequestDTO> users) {
        this.users = users;
    }
}
