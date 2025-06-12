package org.example.persistence.mapper;


import org.example.config.messages.LogMessages;
import org.example.persistence.dto.request.RegisterUserRequestDto;
import org.example.persistence.dto.UserDto;


import org.example.exceptions.ErrorMessages;
import org.example.exceptions.InvalidUserException;
import org.example.persistence.entity.Users;

import java.time.LocalDateTime;

public final class UserMapper {

    private UserMapper() {
    }

    public static Users mapToUser(RegisterUserRequestDto dto) {
        DtoValidator.validateDto(dto, () -> new InvalidUserException(LogMessages.INVALID_DTO), dto.toString());

        DtoValidator.validateNotBlank(dto.email(), () -> new InvalidUserException(ErrorMessages.MISSING_EMAIL));
        DtoValidator.validateNotBlank(dto.username(), () -> new InvalidUserException(ErrorMessages.MISSING_USERNAME));
        DtoValidator.validateNotBlank(dto.password(), () -> new InvalidUserException(ErrorMessages.MISSING_PASSWORD));

        Users user = new Users();
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setLevel(1);

        user.setCreatedAt(LocalDateTime.now());

        return user;
    }

    public static UserDto toUserDto(Users user) {
        return new UserDto(user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getLevel(),
                user.getCreatedAt()
                        .toString());
    }
}
