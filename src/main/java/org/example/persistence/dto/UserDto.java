package org.example.persistence.dto;


import java.util.UUID;

public record UserDto(UUID id, String email, String username, Integer level, String createdAt) {
}
