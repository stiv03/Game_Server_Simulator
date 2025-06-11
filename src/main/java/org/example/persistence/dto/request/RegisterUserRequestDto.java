package org.example.persistence.dto.request;

public record RegisterUserRequestDto(String email, String username, String password) {
}