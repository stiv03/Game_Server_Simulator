package org.example.persistence.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.persistence.dto.GameSessionDto;
import org.example.persistence.dto.request.CreateGameSessionRequestDto;
import org.example.persistence.mapper.GameSessionMapper;
import org.example.persistence.entity.GameSession;
import org.example.persistence.entity.Users;
import org.example.persistence.repository.GameSessionRepository;
import org.example.persistence.service.GameSessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameSessionServiceImpl implements GameSessionService {

    private final GameSessionRepository gameSessionRepository;

    @Override
    @Transactional
    public GameSessionDto createSession(CreateGameSessionRequestDto request, Users creator) {
        GameSession session = GameSessionMapper.mapToGameSession(request, creator);
        GameSession saved = gameSessionRepository.save(session);
        return GameSessionMapper.toGameSessionDto(saved);
    }

    @Override
    @Transactional
    public void endSession(UUID id) {
        gameSessionRepository.findById(id).ifPresent(session -> {
            session.setEndTime(LocalDateTime.now());
            gameSessionRepository.update(session);
        });
    }

    @Override
    public List<GameSessionDto> findAllRunningSessions() {
        return gameSessionRepository.findAllRunning()
                .stream()
                .map(GameSessionMapper::toGameSessionDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GameSessionDto> findById(UUID id) {
        return gameSessionRepository.findById(id)
                .map(GameSessionMapper::toGameSessionDto);
    }

    @Override
    @Transactional
    public Optional<GameSessionDto> joinSession(UUID sessionId, Users user) {
        Optional<GameSession> optionalSession = gameSessionRepository.findById(sessionId);

        if (optionalSession.isEmpty()) {
            return Optional.empty();
        }

        GameSession session = optionalSession.get();

        if (session.getEndTime() != null) {
            return Optional.empty();
        }

        session.getParticipants().add(user);
        gameSessionRepository.update(session);

        return Optional.of(GameSessionMapper.toGameSessionDto(session));
    }

    @Override
    @Transactional
    public Optional<GameSession> leaveSession(UUID sessionId, Users user) {
        Optional<GameSession> optionalSession = gameSessionRepository.findById(sessionId);

        if (optionalSession.isEmpty()) return Optional.empty();

        GameSession session = optionalSession.get();
        session.getParticipants().remove(user);
        return Optional.of(gameSessionRepository.update(session));
    }
}
