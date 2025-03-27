package br.com.pauloultra.desafioluizalabs.repository;

import br.com.pauloultra.desafioluizalabs.entity.CommunicationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunicationRepository extends JpaRepository<CommunicationSchedule, String> {

}
