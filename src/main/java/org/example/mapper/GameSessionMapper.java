package org.example.mapper;


import org.example.config.messages.LogMessages;
import org.example.dto.GameSessionDto;
import org.example.dto.request.CreateGameSessionRequestDto;
import org.example.exceptions.InvalidGameSessionException;
import org.example.persistence.entity.GameSession;
import org.example.persistence.entity.Users;
import org.example.persistence.entity.enums.GameDifficulty;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class GameSessionMapper {

    private GameSessionMapper() {
    }

    public static GameSession mapToGameSession(CreateGameSessionRequestDto dto, Users creator) {
        DtoValidator.validateDto(dto, () -> new InvalidGameSessionException(LogMessages.INVALID_DTO), dto.toString());

        GameSession session = new GameSession();

        session.setCreatedBy(creator);

        session.setStartTime(LocalDateTime.now());


        GameDifficulty difficulty = dto.difficulty() != null ? dto.difficulty() : GameDifficulty.NORMAL;
        session.setDifficulty(difficulty);


        session.getParticipants().add(creator);

        return session;
    }

    public static GameSessionDto toGameSessionDto(GameSession session) {
        Set<UUID> participantIds = session.getParticipants().stream().map(Users::getId).collect(Collectors.toSet());

        boolean isActive = session.getEndTime() == null;

        return new GameSessionDto(session.getId(),
                session.getDifficulty().name(),
                session.getCreatedBy().getId(),
                participantIds,
                isActive,
                session.getStartTime().toString());
    }
}
