package br.com.pauloultra.desafioluizalabs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "communication_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private boolean active = true;

}
