package br.com.pauloultra.desafioluizalabs.dto.response;

import java.time.LocalDateTime;

public record CommunicationResponseDto(
        String guid,
        LocalDateTime scheduledDateTime,
        String recipient,
        String message,
        String type,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}
