package org.example.persistence.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.gameDto.EntityDto;
import org.example.exceptions.SessionNotFoundException;
import org.example.exceptions.UserNotFoundException;
import org.example.game.logic.gameSession.GameSessionManager;
import org.example.game.model.Player;
import org.example.game.service.PlayerService;
import org.example.dto.GameSessionDto;
import org.example.dto.request.CreateGameSessionRequestDto;
import org.example.mapper.GameSessionMapper;
import org.example.persistence.entity.GameSession;
import org.example.persistence.entity.Users;
import org.example.persistence.repository.GameSessionRepository;
import org.example.persistence.repository.UsersRepository;
import org.example.persistence.service.GameSessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameSessionServiceImpl implements GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final UsersRepository usersRepository;
    private final PlayerService playerService;
    private final GameSessionManager sessionManager;

    @Override
    @Transactional
    public GameSessionDto createSession(CreateGameSessionRequestDto request, Users creator) {
        GameSession session = GameSessionMapper.mapToGameSession(request, creator);

        GameSession saved = gameSessionRepository.save(session);

        sessionManager.startSession(saved);

        joinSession(session.getId(), session.getCreatedBy().getId());

        return GameSessionMapper.toGameSessionDto(saved);
    }

    @Override
    @Transactional
    public void endSession(UUID id) {
        sessionManager.stopSession(id);
        gameSessionRepository.findById(id).ifPresent(session -> {
            session.setEndTime(LocalDateTime.now());
            gameSessionRepository.update(session);
        });
    }

    @Override
    @Transactional
    public List<GameSessionDto> findAllRunningSessions() {
        return gameSessionRepository.findAllRunning()
                .stream()
                .map(GameSessionMapper::toGameSessionDto)
                .collect(Collectors.toList());
    }

    @Override
    public GameSessionDto findById(UUID id) {
        return gameSessionRepository.findById(id)
                .map(GameSessionMapper::toGameSessionDto).orElseThrow(SessionNotFoundException::new);
    }

    @Override
    @Transactional
    public GameSessionDto joinSession(UUID sessionId, UUID userId) {
        GameSession session = gameSessionRepository.findById(sessionId).orElseThrow(SessionNotFoundException::new);
        Users user = usersRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        leaveActiveSession(sessionId, user);

        Player player = playerService.registerPlayer(user, session);
        sessionManager.getContext(sessionId).joinPlayer(player);

        session.getParticipants().add(user);
        gameSessionRepository.update(session);

        return GameSessionMapper.toGameSessionDto(session);
    }

    @Override
    @Transactional
    public GameSessionDto leaveSession(UUID sessionId,  UUID userId) {
        GameSession session = gameSessionRepository.findById(sessionId).orElseThrow(SessionNotFoundException::new);
        Users user = usersRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        sessionManager.getOrLoadContext(sessionId).removePlayer(user.getId());

        session.getParticipants().remove(user);
        return GameSessionMapper.toGameSessionDto(gameSessionRepository.update(session));
    }

    @Override
    public List<EntityDto> getRanking(UUID sessionId) {
        return sessionManager.getOrLoadContext(sessionId).getRanking().stream()
                .map(entity -> new EntityDto(entity.getName())).toList();
    }


    private void leaveActiveSession(UUID sessionId, Users user) {
        List<GameSession> activeSessions = gameSessionRepository.findAllRunning();

        for (GameSession active : activeSessions) {
            if (!active.getId().equals(sessionId) && active.getParticipants().contains(user)) {
                leaveSession(active.getId(), user.getId());
                gameSessionRepository.update(active);
            }
        }
    }
}
