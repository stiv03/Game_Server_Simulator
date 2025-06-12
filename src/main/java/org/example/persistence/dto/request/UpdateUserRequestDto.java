package org.example.persistence.dto.request;

public record UpdateUserRequestDto(String email, String username, String password) {
}