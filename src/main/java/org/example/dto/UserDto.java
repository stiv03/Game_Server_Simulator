package org.example.dto;


import java.util.UUID;

public record UserDto(UUID id, String email, String username, Integer level, String createdAt) {
}
