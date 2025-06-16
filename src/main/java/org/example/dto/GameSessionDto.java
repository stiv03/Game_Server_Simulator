package org.example.dto;

import java.util.Set;
import java.util.UUID;

public record GameSessionDto(UUID id, String name, UUID createdBy, Set<UUID> participantIds, Boolean isActive,
                             String createdAt) {
}
