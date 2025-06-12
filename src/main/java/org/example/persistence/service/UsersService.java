package org.example.persistence.service;



import org.example.persistence.dto.request.RegisterUserRequestDto;
import org.example.persistence.dto.request.UpdateUserRequestDto;
import org.example.persistence.dto.UserDto;

import org.example.persistence.entity.Users;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UsersService {

    Users createUser(RegisterUserRequestDto request);

    ResponseEntity<UserDto> getUserByUsername(String username);

    ResponseEntity<UserDto> getUserById(UUID id);

    ResponseEntity<List<UserDto>> getAllUsers();

    ResponseEntity<Void> deleteUser(UUID id);

    ResponseEntity<UserDto> updateUser(UUID id, UpdateUserRequestDto request);

    void levelUpUser(UUID userId);
}
