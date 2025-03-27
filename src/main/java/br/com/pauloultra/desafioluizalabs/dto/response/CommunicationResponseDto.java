package br.com.pauloultra.desafioluizalabs.dto.response;

import java.time.LocalDateTime;

public record CommunicationResponseDto(
        String guid,
        LocalDateTime scheduledDateTime,
        String recipient,
        String message,
        String type,
        String typeDescription,
        String status,
        String statusDescription,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}
