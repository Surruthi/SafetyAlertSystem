package com.safetyalertsystem.controller.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safetyalertsystem.model.authentication.OtpRequestDTO;
import com.safetyalertsystem.model.user.requestdto.UserRequestDTO;
import com.safetyalertsystem.model.user.requestwrapper.UserRequestWrapper;
import com.safetyalertsystem.model.user.responsedto.UserResponseDTO;
import com.safetyalertsystem.model.user.responsewrapper.UserResponseWrapper;
import com.safetyalertsystem.service.user.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResponseWrapper> createUser(@Valid @RequestBody UserRequestWrapper userRequestWrapper) {

        List<UserResponseDTO> userResponseDTOs = new ArrayList<>();

        for (UserRequestDTO userRequestDTO : userRequestWrapper.getUsers()) {

            userResponseDTOs.add(userService.createUser(userRequestDTO));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponseWrapper(userResponseDTOs));
    }

    @PatchMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResponseWrapper> patchUser(@Valid @RequestBody UserRequestWrapper userRequestWrapper) {

        List<UserResponseDTO> userResponseDTOs = new ArrayList<>();

        for (UserRequestDTO userRequestDTO : userRequestWrapper.getUsers()) {

            userResponseDTOs.add(userService.patchUser(userRequestDTO));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new UserResponseWrapper(userResponseDTOs));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {

        UserResponseDTO userResponseDTO = userService.getUserByIdResponse(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {

        userService.deleteUserById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseWrapper> login(@RequestBody OtpRequestDTO request) {
        UserResponseDTO response = userService.login(
                request.getPhoneNumber()
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(new UserResponseWrapper(List.of(response)));
    }
}
