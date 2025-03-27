package br.com.pauloultra.desafioluizalabs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "communication_types")
@Table(indexes = {
        @Index(name = "IDX_GUID_COMMUNICATION_TYPES", columnList = "guid"),
        @Index(name = "IDX_TYPES_CODE_COMMUNICATION_TYPES", columnList = "code")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationType extends BaseEntity{

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
