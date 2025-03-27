package br.com.pauloultra.desafioluizalabs.repository;

import br.com.pauloultra.desafioluizalabs.entity.CommunicationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunicationTypeRepository extends JpaRepository<CommunicationType, String> {
}
