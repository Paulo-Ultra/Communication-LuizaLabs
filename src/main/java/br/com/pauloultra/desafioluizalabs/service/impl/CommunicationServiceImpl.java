package br.com.pauloultra.desafioluizalabs.service.impl;

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
import br.com.pauloultra.desafioluizalabs.service.CommunicationService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class CommunicationServiceImpl implements CommunicationService {

    public static final String PENDING_STATUS = "PENDING";
    public static final String PROCESSING = "PROCESSING";
    public static final String STATUS_NOT_FOUND = "Status not found";
    public static final String SCHEDULE_ALREADY_EXISTS = "An identical schedule with active status already exists";
    public static final String SCHEDULE_NOT_FOUND = "GUID - Schedule Not Found";
    public static final String ALREADY_CANCELLED = "Schedules already cancelled";
    public static final String ALREADY_PROCESSED = "Schedules already processed cannot be cancelled";
    public static final long CANCELLED = 5L;
    public static final String STATUS_CANCELLED_NOT_CONFIGURED = "Canceled status not configured";
    public static final String NOT_POSSIBLE_TO_SAVE_THE_SCHEDULE = "It was not possible to save the schedule.";
    public static final String ERROR_RETRIEVING_RECORDS = "Error retrieving records.";
    public static final String CANCELLING_ERROR_SCHEDULE = "An error occurred while cancelling a schedule";
    private final CommunicationRepository communicationRepository;
    private final CommunicationTypeRepository communicationTypeRepository;
    private final CommunicationStatusRepository communicationStatusRepository;


    public CommunicationServiceImpl(CommunicationRepository communicationRepository, CommunicationTypeRepository communicationTypeRepository,
                                    CommunicationStatusRepository communicationStatusRepository) {
        this.communicationRepository = communicationRepository;
        this.communicationTypeRepository = communicationTypeRepository;
        this.communicationStatusRepository = communicationStatusRepository;
    }

    @Override
    @Transactional
    public CommunicationResponseDto schedule(CommunicationRequestDto request) {
        CommunicationType type = getCommunicationType(request);

        if(communicationRepository.existsByRecipientAndMessageAndStatus_CodeNot(
                request.recipient(),
                request.message(),
                PENDING_STATUS)) {
            throw new StatusException(SCHEDULE_ALREADY_EXISTS);
        }

        CommunicationStatus processingStatus = communicationStatusRepository.findByCode(PROCESSING)
                .orElseThrow(() -> new ResourceNotFoundException(STATUS_NOT_FOUND));

        CommunicationSchedule schedule = getCommunicationSchedule(request, type, processingStatus);

        try {
            CommunicationSchedule savedSchedule = communicationRepository.save(schedule);
            return toResponseDto(savedSchedule);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StatusException(NOT_POSSIBLE_TO_SAVE_THE_SCHEDULE);
        }
    }

    @Override
    public CommunicationResponseDto getByGuid(UUID guid) {
        return toResponseDto(getEntityByGuid((guid)));
    }

    @Override
    public CommunicationStatusResponseDto getStatusByGuid(byte[] guid) {

        CommunicationStatus status = communicationStatusRepository.findStatusByCommunicationGuid(guid)
                .orElseThrow(() -> new ResourceNotFoundException(SCHEDULE_NOT_FOUND));
        return toStatusResponseDto(status);
    }

    @Override
    public Page<CommunicationResponseDto> getAll(int page, int size, String sort, String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        try {
            return communicationRepository.findAll(pageable)
                    .map(this::toResponseDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StatusException(ERROR_RETRIEVING_RECORDS);
        }
    }

    @Override
    public CommunicationResponseDto cancelCommunication(UUID guid) {
        CommunicationSchedule schedule = getEntityByGuid(guid);
        if (schedule.getStatus().getId() == CANCELLED) {
            throw new StatusException(ALREADY_CANCELLED);
        }
        if (schedule.getStatus().isFinalState()) {
            throw new StatusException(ALREADY_PROCESSED);
        }

        CommunicationStatus canceledStatus = communicationStatusRepository.findById(CANCELLED)
                .orElseThrow(() -> new StatusException(STATUS_CANCELLED_NOT_CONFIGURED));

        schedule.setStatus(canceledStatus);
        schedule.setUpdatedAt(LocalDateTime.now());

        try {
            CommunicationSchedule updated = communicationRepository.save(schedule);
            return toResponseDto(updated);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StatusException(CANCELLING_ERROR_SCHEDULE);
        }
    }

    private CommunicationType getCommunicationType(CommunicationRequestDto request) {
        return communicationTypeRepository.findByCode(request.type())
                .orElseThrow(() -> new ResourceNotFoundException(request.type()));
    }

    private static CommunicationSchedule getCommunicationSchedule(CommunicationRequestDto request, CommunicationType type, CommunicationStatus processingStatus) {
        CommunicationSchedule schedule = new CommunicationSchedule();
        schedule.setScheduledDateTime(request.scheduledDateTime());
        schedule.setRecipient(request.recipient());
        schedule.setMessage(request.message());
        schedule.setType(type);
        schedule.setStatus(processingStatus);
        schedule.setCreatedAt(LocalDateTime.now());
        schedule.setUpdatedAt(LocalDateTime.now());
        return schedule;
    }

    private CommunicationResponseDto toResponseDto(CommunicationSchedule entity) {
        return new CommunicationResponseDto(
                entity.getGuid().toString(),
                entity.getScheduledDateTime(),
                entity.getRecipient(),
                entity.getMessage(),
                entity.getType().getCode(),
                entity.getStatus().getCode(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    private CommunicationStatusResponseDto toStatusResponseDto(CommunicationStatus entity) {
        return new CommunicationStatusResponseDto(
                entity.getCode(),
                entity.getDescription(),
                entity.isFinalState());
    }

    private CommunicationSchedule getEntityByGuid(UUID guid) {
        return communicationRepository.findByGuid(guid)
                .orElseThrow(() -> new ResourceNotFoundException(SCHEDULE_NOT_FOUND));
    }
}