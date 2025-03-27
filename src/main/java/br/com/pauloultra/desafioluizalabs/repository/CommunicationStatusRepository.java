package br.com.pauloultra.desafioluizalabs.repository;

import br.com.pauloultra.desafioluizalabs.entity.CommunicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunicationStatusRepository extends JpaRepository<CommunicationStatus, String> {
}
