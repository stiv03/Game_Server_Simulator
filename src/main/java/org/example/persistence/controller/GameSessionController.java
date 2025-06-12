package org.example.persistence.controller;

import lombok.RequiredArgsConstructor;
import org.example.game.model.Entity;
import org.example.persistence.dto.GameSessionDto;
import org.example.persistence.dto.request.CreateGameSessionRequestDto;
import org.example.persistence.mapper.GameSessionMapper;
import org.example.persistence.entity.Users;
import org.example.persistence.repository.UsersRepository;
import org.example.persistence.service.GameSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(ApiRoutes.GameSessionApiRoutes.BASE)
@RequiredArgsConstructor
public class GameSessionController {

    private final GameSessionService gameSessionService;
    private final UsersRepository usersRepository;

    @PostMapping(ApiRoutes.GameSessionApiRoutes.CREATE)
    public ResponseEntity<GameSessionDto> createSession(@RequestParam UUID creatorId, @RequestBody CreateGameSessionRequestDto request) {
        Optional<Users> creator = usersRepository.findById(creatorId);
        if (creator.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        GameSessionDto session = gameSessionService.createSession(request, creator.get());
        return ResponseEntity.ok(session);
    }

    @PostMapping(ApiRoutes.GameSessionApiRoutes.JOIN)
    public ResponseEntity<GameSessionDto> joinSession(@PathVariable UUID id, @RequestParam UUID userId) {
        Optional<Users> user = usersRepository.findById(userId);
        return user.map(users -> gameSessionService.joinSession(id, users).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build())).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping(ApiRoutes.GameSessionApiRoutes.BY_ID)
    public ResponseEntity<GameSessionDto> getById(@PathVariable UUID id) {
        return gameSessionService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping(ApiRoutes.GameSessionApiRoutes.RUNNING)
    public ResponseEntity<List<GameSessionDto>> getAllRunningSessions() {
        List<GameSessionDto> sessions = gameSessionService.findAllRunningSessions().stream().toList();

        return ResponseEntity.ok(sessions);
    }

    @PostMapping(ApiRoutes.GameSessionApiRoutes.LEAVE)
    public ResponseEntity<GameSessionDto> leaveSession(@PathVariable UUID id, @RequestParam UUID userId) {
        Optional<Users> user = usersRepository.findById(userId);
        return user
                .flatMap(u -> gameSessionService.leaveSession(id, u)
                        .map(session -> ResponseEntity.ok(GameSessionMapper.toGameSessionDto(session))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(ApiRoutes.GameSessionApiRoutes.STOP)
    public ResponseEntity<Void> stopSession(@PathVariable UUID id) {
        gameSessionService.endSession(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(ApiRoutes.GameSessionApiRoutes.RANKING)
    public ResponseEntity<List<Entity>> getRanking(@PathVariable UUID id) {
        return ResponseEntity.ok(gameSessionService.getRanking(id));
    }
}
