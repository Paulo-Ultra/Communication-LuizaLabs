package br.com.pauloultra.desafioluizalabs.controller.impl;

import br.com.pauloultra.desafioluizalabs.controller.CommunicationApi;
import br.com.pauloultra.desafioluizalabs.dto.request.CommunicationRequestDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationResponseDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationStatusResponseDto;
import br.com.pauloultra.desafioluizalabs.service.CommunicationService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommunicationController implements CommunicationApi {

    private final CommunicationService communicationService;

    public CommunicationController(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @Override
    public CommunicationResponseDto schedule(CommunicationRequestDto request) {
        return null;
    }

    @Override
    public CommunicationResponseDto getByGuid(String guid) {
        return null;
    }

    @Override
    public CommunicationStatusResponseDto getStatusByGuid(String guid) {
        return null;
    }

    @Override
    public List<CommunicationResponseDto> getAll() {
        return List.of();
    }

    @Override
    public void delete(String guid) {

    }
}
