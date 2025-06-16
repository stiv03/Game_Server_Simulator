package org.example.persistence.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.gameDto.EntityDto;
import org.example.dto.GameSessionDto;
import org.example.dto.request.CreateGameSessionRequestDto;
import org.example.mapper.GameSessionMapper;
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
        return ResponseEntity.ok(gameSessionService.joinSession(id, userId));

    }

    @GetMapping(ApiRoutes.GameSessionApiRoutes.BY_ID)
    public ResponseEntity<GameSessionDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(gameSessionService.findById(id));
    }


    @GetMapping(ApiRoutes.GameSessionApiRoutes.RUNNING)
    public ResponseEntity<List<GameSessionDto>> getAllRunningSessions() {
        List<GameSessionDto> sessions = gameSessionService.findAllRunningSessions();
        return ResponseEntity.ok(sessions);
    }

    @PostMapping(ApiRoutes.GameSessionApiRoutes.LEAVE)
    public ResponseEntity<GameSessionDto> leaveSession(@PathVariable UUID id, @RequestParam UUID userId) {
        return ResponseEntity.ok( gameSessionService.leaveSession(id, userId));
    }

    @PostMapping(ApiRoutes.GameSessionApiRoutes.STOP)
    public ResponseEntity<Void> stopSession(@PathVariable UUID id) {
        gameSessionService.endSession(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(ApiRoutes.GameSessionApiRoutes.RANKING)
    public ResponseEntity<List<EntityDto>> getRanking(@PathVariable UUID id) {
        return ResponseEntity.ok(gameSessionService.getRanking(id));
    }
}
