package org.example.persistence.controller;


import org.example.persistence.dto.request.RegisterUserRequestDto;
import org.example.persistence.dto.request.UpdateUserRequestDto;
import org.example.persistence.dto.UserDto;

import org.example.persistence.entity.Users;
import org.example.persistence.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiRoutes.UserApiRoutes.BASE)
public class UserController {

    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping(ApiRoutes.AuthApiRoutes.REGISTER)
    public ResponseEntity<Users> register(@RequestBody RegisterUserRequestDto request) {
        Users user = usersService.createUser(request);
        return ResponseEntity.ok(user);
    }

    @GetMapping(ApiRoutes.UserApiRoutes.BY_ID)
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        return usersService.getUserById(id);
    }

    @GetMapping(ApiRoutes.UserApiRoutes.BY_USERNAME)
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return usersService.getUserByUsername(username);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return usersService.getAllUsers();
    }

    @PutMapping(ApiRoutes.UserApiRoutes.BY_ID)
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequestDto request) {
        return usersService.updateUser(id, request);
    }

    @DeleteMapping(ApiRoutes.UserApiRoutes.BY_ID)
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        return usersService.deleteUser(id);
    }
}