package br.com.pauloultra.desafioluizalabs.repository;

import br.com.pauloultra.desafioluizalabs.entity.CommunicationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunicationTypeRepository extends JpaRepository<CommunicationType, Long> {
    Optional<CommunicationType> findByCode(String code);
}
