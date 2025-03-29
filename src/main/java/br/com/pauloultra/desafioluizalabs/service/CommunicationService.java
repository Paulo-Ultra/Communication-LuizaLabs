package br.com.pauloultra.desafioluizalabs.service;

import br.com.pauloultra.desafioluizalabs.dto.request.CommunicationRequestDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationResponseDto;
import br.com.pauloultra.desafioluizalabs.dto.response.CommunicationStatusResponseDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CommunicationService {
    CommunicationResponseDto schedule(CommunicationRequestDto request);
    CommunicationResponseDto getByGuid(UUID guid);
    CommunicationStatusResponseDto getStatusByGuid(byte[] guid);
    Page<CommunicationResponseDto> getAll(int page, int size, String sort, String sortDirection);
    CommunicationResponseDto cancelCommunication(UUID guid);
}
