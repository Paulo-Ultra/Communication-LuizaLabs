package br.com.pauloultra.desafioluizalabs.service.impl;

import br.com.pauloultra.desafioluizalabs.dto.request.CommunicationRequestDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationResponseDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationStatusResponseDto;
import br.com.pauloultra.desafioluizalabs.repository.CommunicationRepository;
import br.com.pauloultra.desafioluizalabs.service.CommunicationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationServiceImpl implements CommunicationService {

    private final CommunicationRepository communicationRepository;

    public CommunicationServiceImpl(CommunicationRepository communicationRepository) {
        this.communicationRepository = communicationRepository;
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
