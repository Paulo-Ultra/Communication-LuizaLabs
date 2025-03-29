package br.com.pauloultra.desafioluizalabs.service;

import br.com.pauloultra.desafioluizalabs.dto.request.CommunicationRequestDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationResponseDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationStatusResponseDto;
import br.com.pauloultra.desafioluizalabs.entity.CommunicationSchedule;
import br.com.pauloultra.desafioluizalabs.entity.CommunicationStatus;
import br.com.pauloultra.desafioluizalabs.entity.CommunicationType;
import br.com.pauloultra.desafioluizalabs.exception.ResourceNotFoundException;
import br.com.pauloultra.desafioluizalabs.exception.StatusException;
import br.com.pauloultra.desafioluizalabs.repository.CommunicationRepository;
import br.com.pauloultra.desafioluizalabs.repository.CommunicationStatusRepository;
import br.com.pauloultra.desafioluizalabs.repository.CommunicationTypeRepository;
import br.com.pauloultra.desafioluizalabs.service.impl.CommunicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.pauloultra.desafioluizalabs.service.impl.CommunicationServiceImpl.CANCELED;
import static br.com.pauloultra.desafioluizalabs.service.impl.CommunicationServiceImpl.ERROR_RETRIEVING_RECORDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunicationServiceTest {

    @InjectMocks
    private CommunicationServiceImpl communicationService;

    @Mock
    private CommunicationRepository communicationRepository;

    @Mock
    private CommunicationStatusRepository communicationStatusRepository;

    @Mock
    private CommunicationTypeRepository communicationTypeRepository;

    private final UUID testGuid = UUID.randomUUID();
    private final LocalDateTime testDate = LocalDateTime.now();

    @BeforeEach
    public void initTest(){
        this.communicationService = new CommunicationServiceImpl(
                communicationRepository, communicationTypeRepository, communicationStatusRepository);
    }

    @Test
    void shouldCreateACommunicationScheduleSuccessfully(){

        CommunicationRequestDto request = new CommunicationRequestDto(
                testDate.plusDays(1), "test@example.com", "Test message", "EMAIL", "PENDING");


        CommunicationType type = new CommunicationType(1L, "EMAIL", "Email communication", true);
        CommunicationStatus status = new CommunicationStatus(1L, "PENDING", "Pending status", false);
        CommunicationSchedule savedEntity = createTestScheduleEntity();

        when(communicationTypeRepository.findByCode("EMAIL")).thenReturn(Optional.of(type));
        when(communicationStatusRepository.findByCode("PROCESSING")).thenReturn(Optional.of(status));
        when(communicationRepository.existsByRecipientAndMessageAndStatus_CodeNot(
                any(), any(), any())).thenReturn(false);
        when(communicationRepository.save(any())).thenReturn(savedEntity);

        CommunicationResponseDto result = communicationService.schedule(request);

        assertNotNull(result);
        assertEquals("test@example.com", result.recipient());
        verify(communicationRepository, times(1)).save(any());
    }

    @Test
    void ShouldThrowExceptionOnCreateACommunicationScheduleFail(){

        CommunicationRequestDto request = new CommunicationRequestDto(
                testDate.plusDays(1), "test@example.com", "Test message", "EMAIL", "PENDING");

        CommunicationType type = new CommunicationType(1L, "EMAIL", "Comunicação por e-mail", true);
        CommunicationStatus status = new CommunicationStatus(1L, "PENDING", "Agendamento pendente", false);

        when(communicationTypeRepository.findByCode("EMAIL")).thenReturn(Optional.of(type));
        when(communicationStatusRepository.findByCode("PROCESSING")).thenReturn(Optional.of(status));
        when(communicationRepository.existsByRecipientAndMessageAndStatus_CodeNot(
                any(), any(), any())).thenReturn(false);

        when(communicationRepository.save(any())).thenThrow(new RuntimeException());

        assertThrows(StatusException.class, () -> communicationService.schedule(request));

    }

    @Test
    void schedule_ShouldThrowStatusExceptionWhenDuplicateActiveScheduleExists() {

        CommunicationRequestDto request = new CommunicationRequestDto(
                testDate.plusDays(1),
                "test@example.com",
                "Test message",
                "EMAIL",
                "PENDING"
        );

        CommunicationType type = new CommunicationType(1L, "EMAIL", "Comunicação por e-mail", true);

        when(communicationTypeRepository.findByCode("EMAIL")).thenReturn(Optional.of(type));
        when(communicationRepository.existsByRecipientAndMessageAndStatus_CodeNot(
                request.recipient(),
                request.message(),
                CommunicationServiceImpl.PENDING_STATUS
        )).thenReturn(true);

        StatusException exception = assertThrows(StatusException.class,
                () -> communicationService.schedule(request));

        assertEquals(CommunicationServiceImpl.SCHEDULE_ALREADY_EXISTS, exception.getMessage());

        verify(communicationTypeRepository).findByCode("EMAIL");
        verify(communicationRepository).existsByRecipientAndMessageAndStatus_CodeNot(
                request.recipient(),
                request.message(),
                CommunicationServiceImpl.PENDING_STATUS
        );
        verify(communicationRepository, never()).save(any());
    }

    @Test
    void getByGuidShouldReturnCommunicationSchedule() {

        CommunicationSchedule entity = createTestScheduleEntity();
        when(communicationRepository.findByGuid(testGuid)).thenReturn(Optional.of(entity));

        CommunicationResponseDto result = communicationService.getByGuid(testGuid);

        assertNotNull(result);
        assertEquals(testGuid.toString(), result.guid());
    }

    @Test
    void getByGuidShouldThrowResourceNotFoundExceptionWhenNotFound() {

        when(communicationRepository.findByGuid(testGuid)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> communicationService.getByGuid(testGuid));
    }

    @Test
    void getStatusByGuidShouldReturnStatus() {

        CommunicationStatus status = new CommunicationStatus(1L, "PROCESSING", "Em processamento", false);
        when(communicationStatusRepository.findStatusByCommunicationGuid(any()))
                .thenReturn(Optional.of(status));

        CommunicationStatusResponseDto result = communicationService.getStatusByGuid(testGuid.toString().getBytes());

        assertNotNull(result);
        assertEquals("PROCESSING", result.code());
    }

    @Test
    void getStatusByGuidShouldThrowResourceNotFoundExceptionWhenNotFound() {

        when(communicationStatusRepository.findStatusByCommunicationGuid(any()))
                .thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class,
                () -> communicationService.getStatusByGuid(testGuid.toString().getBytes()));
    }

    @Test
    void getAllShouldIncludeOriginalMessageInException() {

        when(communicationRepository.findAll(any(Pageable.class)))
                .thenThrow(new RuntimeException(ERROR_RETRIEVING_RECORDS));


        Exception exception = assertThrows(StatusException.class,
                () -> communicationService.getAll(0, 10, "createdAt", "desc"));

        assertEquals(ERROR_RETRIEVING_RECORDS, exception.getMessage());
    }

    @Test
    void getAllShouldReturnPagedResults() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<CommunicationSchedule> pagedResponse = new PageImpl<>(List.of(createTestScheduleEntity()));

        when(communicationRepository.findAll(pageable)).thenReturn(pagedResponse);

        Page<CommunicationResponseDto> result = communicationService.getAll(0, 10, "createdAt", "desc");

        assertEquals(1, result.getTotalElements());
        verify(communicationRepository, times(1)).findAll(pageable);
    }

    @Test
    void cancelCommunicationShouldCancelPendingSchedule() {

        CommunicationSchedule schedule = createTestScheduleEntity();
        CommunicationStatus canceledStatus = new CommunicationStatus(5L, "CANCELED", "Cancelado pelo usuário", true);

        when(communicationRepository.findByGuid(testGuid)).thenReturn(Optional.of(schedule));
        when(communicationStatusRepository.findById(5L)).thenReturn(Optional.of(canceledStatus));
        when(communicationRepository.save(any())).thenReturn(schedule);

        CommunicationResponseDto result = communicationService.cancelCommunication(testGuid);

        assertEquals("CANCELED", result.status());
        verify(communicationRepository, times(1)).save(schedule);
    }

    @Test
    void cancelCommunicationShouldThrowWhenAlreadyCanceled() {

        CommunicationSchedule schedule = createTestScheduleEntity();
        schedule.setStatus(new CommunicationStatus(5L, "CANCELED", "Cancelado pelo usuário", true));

        when(communicationRepository.findByGuid(testGuid)).thenReturn(Optional.of(schedule));

        assertThrows(StatusException.class,
                () -> communicationService.cancelCommunication(testGuid));
    }

    @Test
    void cancelCommunicationShouldThrowWhenAlreadyProcessed() {

        CommunicationSchedule schedule = createTestScheduleEntity();
        schedule.setStatus(new CommunicationStatus(2L, "SENT", "Enviado com sucesso", true));

        when(communicationRepository.findByGuid(testGuid)).thenReturn(Optional.of(schedule));

        assertThrows(StatusException.class,
                () -> communicationService.cancelCommunication(testGuid));
    }

    @Test
    void cancelCommunication_ShouldThrowWhenCancelStatusNotFound() {

        CommunicationSchedule schedule = createTestScheduleEntity();

        when(communicationRepository.findByGuid(testGuid)).thenReturn(Optional.of(schedule));
        when(communicationStatusRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(StatusException.class,
                () -> communicationService.cancelCommunication(testGuid));
    }

    @Test
    void cancelCommunicationShouldLogWarningWhenAlreadyCanceled() {

        CommunicationSchedule schedule = createTestScheduleEntity();
        schedule.setStatus(new CommunicationStatus(CANCELED, "CANCELED", "Cancelado pelo usuário", true));

        when(communicationRepository.findByGuid(testGuid)).thenReturn(Optional.of(schedule));

        assertThrows(StatusException.class,
                () -> communicationService.cancelCommunication(testGuid));

    }

    @Test
    void cancelCommunicationShouldLogErrorWhenSaveFails() {

        CommunicationSchedule schedule = createTestScheduleEntity();

        when(communicationRepository.findByGuid(testGuid)).thenReturn(Optional.of(schedule));
        when(communicationStatusRepository.findById(CANCELED)).thenReturn(Optional.of(new CommunicationStatus()));
        when(communicationRepository.save(any())).thenThrow(new RuntimeException());

        assertThrows(StatusException.class,
                () -> communicationService.cancelCommunication(testGuid));
    }

    private CommunicationSchedule createTestScheduleEntity() {
        CommunicationType type = new CommunicationType(1L, "EMAIL", "Comunicação por e-mail", true);
        CommunicationStatus status = new CommunicationStatus(1L, "PENDING", "Agendamento pendente", false);

        CommunicationSchedule entity = new CommunicationSchedule();
        entity.setGuid(testGuid);
        entity.setScheduledDateTime(testDate.plusDays(1));
        entity.setRecipient("test@example.com");
        entity.setMessage("Test message");
        entity.setType(type);
        entity.setStatus(status);
        entity.setCreatedAt(testDate);
        entity.setUpdatedAt(testDate);

        return entity;
    }
}
