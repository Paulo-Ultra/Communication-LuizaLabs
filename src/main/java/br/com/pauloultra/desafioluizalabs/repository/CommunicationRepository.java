package br.com.pauloultra.desafioluizalabs.repository;

import br.com.pauloultra.desafioluizalabs.entity.CommunicationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommunicationRepository extends JpaRepository<CommunicationSchedule, String> {

    boolean existsByRecipientAndMessageAndStatus_CodeNot(String recipient, String message, String statusCode);
    Optional<CommunicationSchedule> findByGuid(UUID guid);
}