package br.com.pauloultra.desafioluizalabs.controller;

import br.com.pauloultra.desafioluizalabs.dto.request.CommunicationRequestDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationResponseDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationStatusResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Communication Scheduler", description = "API para agendamento de comunicações LuizaLabs")
@RequestMapping("/api/v1/communications")
public interface CommunicationApi {


    @ApiResponse(responseCode = "201", description = "Comunicação agendada com sucesso",
            content = @Content(schema = @Schema(implementation = CommunicationResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Tipo ou status de comunicação não encontrado")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    CommunicationResponseDto schedule(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para agendamento da comunicação",
            required = true,
            content = @Content(schema = @Schema(implementation = CommunicationRequestDto.class))
    )
            @Valid @RequestBody CommunicationRequestDto request);

    @Operation(summary = "Consultar agendamento",
            description = "Recupera os detalhes de um agendamento de comunicação específico")
    @ApiResponse(responseCode = "200", description = "Agendamento encontrado",
            content = @Content(schema = @Schema(implementation = CommunicationResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    @GetMapping(value = "/{guid}", produces = MediaType.APPLICATION_JSON_VALUE)
    CommunicationResponseDto getByGuid(
            @Parameter(description = "Busca do agendamento pelo GUID", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID guid);

    @Operation(summary = "Consultar status", description = "Recupera o status atual de um agendamento")
    @ApiResponse(responseCode = "200", description = "Status encontrado",
            content = @Content(schema = @Schema(implementation = CommunicationStatusResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    @GetMapping(value = "/{guid}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    CommunicationStatusResponseDto getStatusByGuid(
            @Parameter(description = "GUID do agendamento", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable byte[] guid);

    @Operation(summary = "Listar todos agendamentos", description = "Recupera todos os agendamentos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de agendamentos",
            content = @Content(schema = @Schema(implementation = CommunicationResponseDto[].class)))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    Page<CommunicationResponseDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String sortDirection);

    @Operation(summary = "Cancelar agendamento", description = "Cancela um agendamento não processado")
    @ApiResponse(responseCode = "204", description = "Agendamento cancelado com sucesso")
    @ApiResponse(responseCode = "400", description = "Agendamento já processado não pode ser cancelado")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    @DeleteMapping("/{guid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<CommunicationResponseDto> delete(
            @Parameter(description = "GUID do agendamento a ser cancelado", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID guid);
}
