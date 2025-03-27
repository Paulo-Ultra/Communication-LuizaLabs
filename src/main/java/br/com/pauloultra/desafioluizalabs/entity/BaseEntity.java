package br.com.pauloultra.desafioluizalabs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "guid", columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    private String guid;
}
