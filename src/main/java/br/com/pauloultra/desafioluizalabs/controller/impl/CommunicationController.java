package br.com.pauloultra.desafioluizalabs.controller.impl;

import br.com.pauloultra.desafioluizalabs.controller.CommunicationApi;
import br.com.pauloultra.desafioluizalabs.dto.request.CommunicationRequestDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationResponseDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationStatusResponseDto;
import br.com.pauloultra.desafioluizalabs.service.CommunicationService;
import br.com.pauloultra.desafioluizalabs.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CommunicationController implements CommunicationApi {

    private final CommunicationService communicationService;

    public CommunicationController(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @Override
    public CommunicationResponseDto schedule(CommunicationRequestDto request) {
        return communicationService.schedule(request);
    }

    @Override
    public CommunicationResponseDto getByGuid(UUID guid) {
        return communicationService.getByGuid(guid);
    }

    @Override
    public CommunicationStatusResponseDto getStatusByGuid(String guid) {
        byte[] guidBytes = Utils.convertGuidStringToBytes(guid);
        return communicationService.getStatusByGuid(guidBytes);
    }

    @Override
    public Page<CommunicationResponseDto> getAll(int page, int size, String sort, String sortDirection) {
        return communicationService.getAll(page, size, sort, sortDirection);
    }

    @Override
    public ResponseEntity<CommunicationResponseDto> delete(UUID guid) {
        CommunicationResponseDto canceledSchedule = communicationService.cancelCommunication(guid);
        return ResponseEntity.ok(canceledSchedule);
    }
}
