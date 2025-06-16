package org.example.persistence.service;

import org.example.dto.gameDto.EntityDto;
import org.example.dto.GameSessionDto;
import org.example.dto.request.CreateGameSessionRequestDto;
import org.example.persistence.entity.GameSession;
import org.example.persistence.entity.Users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameSessionService {

    GameSessionDto createSession(CreateGameSessionRequestDto request, Users creator);

    void endSession(UUID id);

    List<GameSessionDto> findAllRunningSessions();

    GameSessionDto findById(UUID id);

    GameSessionDto joinSession(UUID sessionId, UUID userId);

    GameSessionDto leaveSession(UUID sessionId, UUID userId);

    List<EntityDto> getRanking(UUID sessionId);
}
