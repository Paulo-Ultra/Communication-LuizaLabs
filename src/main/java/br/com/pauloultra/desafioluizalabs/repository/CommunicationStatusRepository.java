package br.com.pauloultra.desafioluizalabs.repository;

import br.com.pauloultra.desafioluizalabs.entity.CommunicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunicationStatusRepository extends JpaRepository<CommunicationStatus, Long> {
    Optional<CommunicationStatus> findByCode(String code);

    @Query(value = "SELECT cs.* FROM communication_statuses cs " +
            "JOIN communication_schedules c ON cs.id = c.status_id " +
            "WHERE c.guid = UUID_TO_BIN(:guid)",
            nativeQuery = true)
    Optional<CommunicationStatus> findStatusByCommunicationGuid(@Param("guid") byte[] communicationGuid);
}
