package br.com.pauloultra.desafioluizalabs.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CommunicationRequestDto(
        @NotNull(message = "The date cannot be null.")
        @Future(message = "The date must be in the future.")
        LocalDateTime scheduledDateTime,
        @NotBlank(message = "Recipient field must be filled in")
        @Size(max = 255)
        String recipient,
        @NotBlank(message = "Message field must be filled in")
        @Size(max = 1000)
        String message,
        @NotBlank(message = "The type of message sending must be filled in")
        String type,
        @NotBlank(message = "Appointment status must be filled in")
        String status) {}
