package br.com.pauloultra.desafioluizalabs.controller;

import br.com.pauloultra.desafioluizalabs.controller.impl.CommunicationController;
import br.com.pauloultra.desafioluizalabs.dto.request.CommunicationRequestDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationResponseDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationStatusResponseDto;
import br.com.pauloultra.desafioluizalabs.service.CommunicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunicationController.class)
class CommunicationControllerTest {

    @MockitoBean
    private CommunicationService communicationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID testGuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private final LocalDateTime testDate = LocalDateTime.now();

    @Test
    void scheduleShouldReturn201() throws Exception {

        CommunicationRequestDto request = new CommunicationRequestDto(
                testDate.plusDays(1), "test@example.com", "Test message", "EMAIL", "PENDING");

        CommunicationResponseDto response = createTestResponseDto();

        when(communicationService.schedule(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/communications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.guid").exists());
    }

    @Test
    void schedule_ShouldReturn400WhenInvalidInput() throws Exception {

        String invalidRequest = """
        {
            "scheduledDateTime": null,
            "recipient": "não-é-email",
            "message": "",
            "type": null
        }
        """;

        mockMvc.perform(post("/api/v1/communications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void schedule_ShouldReturn400WhenFutureDateRequired() throws Exception {
        // Data no passado
        String invalidRequest = """
        {
            "scheduledDateTime": "2020-01-01T00:00:00",
            "recipient": "email@valido.com",
            "message": "Mensagem válida",
            "type": "EMAIL"
        }
        """;

        mockMvc.perform(post("/api/v1/communications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void scheduleShouldReturn404() throws Exception {

        CommunicationRequestDto request = new CommunicationRequestDto(
                testDate.plusDays(1), "test@example.com", "Test message", "EMAIL", "PENDING");

        CommunicationResponseDto response = createTestResponseDto();

        when(communicationService.schedule(any())).thenReturn(response);

        mockMvc.perform(post("/api/communications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByGuidShouldReturn200() throws Exception {

        CommunicationResponseDto dto = createTestResponseDto();
        when(communicationService.getByGuid(testGuid)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/communications/{guid}", testGuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guid").value(testGuid.toString()));
    }

    @Test
    void getByGuid_ShouldReturn400ForInvalidGuidFormat() throws Exception {
        mockMvc.perform(get("/api/v1/communications/{guid}", "guid-invalido"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByGuidShouldReturn404() throws Exception {

        CommunicationResponseDto dto = createTestResponseDto();
        when(communicationService.getByGuid(testGuid)).thenReturn(dto);

        mockMvc.perform(get("/api/communications/{guid}", testGuid))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStatusByGuidShouldReturn200() throws Exception {

        UUID guid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        byte[] guidBytes = guid.toString().getBytes(StandardCharsets.UTF_8);

        CommunicationStatusResponseDto expectedResponse = new CommunicationStatusResponseDto(
                "PROCESSING",
                "Em processamento",
                false
        );

        when(communicationService.getStatusByGuid(guidBytes))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/communications/{guid}/status", guid)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getStatusByGuid_WithValidGuid_ShouldReturn200() throws Exception {
        UUID validGuid = UUID.randomUUID();
        CommunicationStatusResponseDto response = new CommunicationStatusResponseDto("PENDING", "Pending", false);

        when(communicationService.getStatusByGuid(any(byte[].class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/communications/{guid}/status", validGuid))
                .andExpect(status().isOk());
    }

    @Test
    void getStatusByGuid_ShouldReturn400ForInvalidGuidFormat() throws Exception {
        mockMvc.perform(get("/api/v1/communications/{guid}/status", "guid_invalido"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStatusByGuidShouldReturn404() throws Exception {

        UUID guid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        byte[] guidBytes = guid.toString().getBytes(StandardCharsets.UTF_8);

        CommunicationStatusResponseDto expectedResponse = new CommunicationStatusResponseDto(
                "PROCESSING",
                "Em processamento",
                false
        );

        when(communicationService.getStatusByGuid(guidBytes))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/api/communications/{guid}/status", guid)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllShouldReturn200() throws Exception {

        CommunicationResponseDto dto = createTestResponseDto();
        Page<CommunicationResponseDto> page = new PageImpl<>(List.of(dto));

        when(communicationService.getAll(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/communications")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt")
                        .param("sortDirection", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].guid").value(testGuid.toString()));
    }

    @Test
    void getAllShouldReturn400ForInvalidPagination() throws Exception {

        mockMvc.perform(get("/api/v1/communications")
                        .param("page", "campoInvalido")
                        .param("size", "campoInvalido"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/communications")
                        .param("page", "campoInvalido")
                        .param("size", "campoInvalido"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllShouldReturn404() throws Exception {

        CommunicationResponseDto dto = createTestResponseDto();
        Page<CommunicationResponseDto> page = new PageImpl<>(List.of(dto));

        when(communicationService.getAll(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(page);

        mockMvc.perform(get("/api/communications")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt")
                        .param("sortDirection", "desc"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cancelCommunicationShouldReturn200() throws Exception {

        CommunicationResponseDto response = createTestResponseDtoToDelete();

        when(communicationService.cancelCommunication(testGuid)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/communications/{guid}", testGuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELED"));
    }

    @Test
    void cancelCommunicationShouldReturn400ForInvalidGuidFormat() throws Exception {
        mockMvc.perform(delete("/api/v1/communications/{guid}", "guid_invalido"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelCommunicationShouldReturn404() throws Exception {

        CommunicationResponseDto response = createTestResponseDtoToDelete();

        when(communicationService.cancelCommunication(testGuid)).thenReturn(response);

        mockMvc.perform(delete("/api/communications/{guid}", testGuid))
                .andExpect(status().isNotFound());
    }

    private CommunicationResponseDto createTestResponseDto() {
        return new CommunicationResponseDto(
                testGuid.toString(),
                testDate.plusDays(1),
                "test@example.com",
                "Test message",
                "EMAIL",
                "PENDING",
                testDate,
                testDate
        );
    }

    private CommunicationResponseDto createTestResponseDtoToDelete() {
        return new CommunicationResponseDto(
                testGuid.toString(),
                testDate.plusDays(1),
                "test@example.com",
                "Test message",
                "EMAIL",
                "CANCELED",
                testDate,
                testDate
        );
    }
}