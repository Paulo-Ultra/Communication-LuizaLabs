package br.com.pauloultra.desafioluizalabs.service;

import br.com.pauloultra.desafioluizalabs.dto.request.CommunicationRequestDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationResponseDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationStatusResponseDto;

import java.util.List;

public interface CommunicationService {
    CommunicationResponseDto schedule(CommunicationRequestDto request);
    CommunicationResponseDto getByGuid(String guid);
    CommunicationStatusResponseDto getStatusByGuid(String guid);
    List<CommunicationResponseDto> getAll();
    void delete(String guid);
}
