package org.example.persistence.service;

import org.example.persistence.dto.GameSessionDto;
import org.example.persistence.dto.request.CreateGameSessionRequestDto;
import org.example.persistence.entity.GameSession;
import org.example.persistence.entity.Users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameSessionService {

    GameSessionDto createSession(CreateGameSessionRequestDto request, Users creator);

    void endSession(UUID id);

    List<GameSessionDto> findAllRunningSessions();

    Optional<GameSessionDto> findById(UUID id);

    Optional<GameSessionDto> joinSession(UUID sessionId, Users user);

    Optional<GameSession> leaveSession(UUID sessionId, Users user);
}
