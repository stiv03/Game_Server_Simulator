package org.example.dto.request;

import org.example.persistence.entity.enums.GameDifficulty;

public record CreateGameSessionRequestDto(GameDifficulty difficulty) {
}
