package br.com.pauloultra.desafioluizalabs.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CommunicationRequestDto(
        @NotNull @Future LocalDateTime scheduledDateTime,
        @NotBlank @Size(max = 255) String recipient,
        @NotBlank @Size(max = 1000) String message,
        @NotBlank String type) {}
