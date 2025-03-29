package br.com.pauloultra.desafioluizalabs.dto.response;

public record CommunicationStatusResponseDto(
        String code,
        String description,
        boolean isFinalState) {}