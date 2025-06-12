package org.example.persistence.service.impl;

import org.example.config.messages.LogMessages;
import org.example.persistence.dto.request.RegisterUserRequestDto;
import org.example.persistence.dto.request.UpdateUserRequestDto;
import org.example.persistence.dto.UserDto;
import org.example.exceptions.UserAlreadyExistsException;
import org.example.exceptions.UserNotFoundException;
import org.example.persistence.mapper.UserMapper;
import org.example.persistence.entity.Users;
import org.example.persistence.repository.UsersRepository;
import org.example.persistence.service.UsersService;
import org.example.persistence.service.utils.OptionalSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users createUser(RegisterUserRequestDto request) {
        logger.info(LogMessages.ADDING_USER, request.username());

        validateUserDoesNotExist(request);

        Users user = UserMapper.mapToUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return usersRepository.save(user);
    }

    @Override
    public ResponseEntity<UserDto> getUserById(UUID id) {
        logger.info(LogMessages.FETCHING_USER_BY_ID, id);

        Users user = getUser(id);

        return ResponseEntity.ok(UserMapper.toUserDto(user));
    }

    @Override
    public ResponseEntity<UserDto> getUserByUsername(String username) {
        logger.info(LogMessages.FETCHING_USER_BY_USERNAME, username);

        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn(LogMessages.USER_NOT_FOUND_BY_USERNAME, username);
                    return new UserNotFoundException();
                });

        return ResponseEntity.ok(UserMapper.toUserDto(user));
    }

    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        logger.info(LogMessages.FETCHING_ALL_USERS);

        List<UserDto> allUsers = usersRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();

        return ResponseEntity.ok(allUsers);

    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        logger.info(LogMessages.DELETING_USER, id);

        Users user = getUser(id);

        usersRepository.delete(user);
        return ResponseEntity.noContent()
                .build();
    }

    @Override
    public ResponseEntity<UserDto> updateUser(UUID id, UpdateUserRequestDto request) {
        logger.info(LogMessages.UPDATING_USER, id);

        Users userToUpdate = getUser(id);

        OptionalSetter.consumeIfPresent(Optional.ofNullable(request.email()), userToUpdate::setEmail);
        OptionalSetter.consumeIfPresent(Optional.ofNullable(request.username()), userToUpdate::setUsername);

        OptionalSetter.consumeIfPresent(Optional.ofNullable(request.password()),
                password -> userToUpdate.setPassword(passwordEncoder.encode(password)));

        Users updated = usersRepository.update(userToUpdate);
        logger.info(LogMessages.UPDATED_USER, id, updated);

        return ResponseEntity.ok(UserMapper.toUserDto(updated));
    }

    @Override
    public void levelUpUser(UUID userId) {
        Users user = getUser(userId);
        user.setLevel(user.getLevel() + 1);

        logger.info(LogMessages.USER_LEVEL_UP, userId, user.getLevel());

        usersRepository.update(user);
    }


    private void validateUserDoesNotExist(RegisterUserRequestDto request) {
        if (usersRepository.isEmailRegistered(request.email())) {
            throw new UserAlreadyExistsException(request.email());
        }

        if (usersRepository.isUsernameRegistered(request.username())) {
            throw new UserAlreadyExistsException(request.username());
        }
    }

    private Users getUser(UUID userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn(LogMessages.USER_NOT_FOUND_BY_ID, userId);
                    return new UserNotFoundException();
                });
    }

    private Users getLoggedInUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return usersRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }
}
