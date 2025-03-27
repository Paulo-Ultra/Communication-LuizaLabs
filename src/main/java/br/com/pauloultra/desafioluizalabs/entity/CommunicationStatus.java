package br.com.pauloultra.desafioluizalabs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "communication_statuses")
@Table(indexes = {
        @Index(name = "IDX_GUID_COMMUNICATION_STATUSES", columnList = "guid"),
        @Index(name = "IDX_STATUSES_CODE_COMMUNICATION_STATUSES", columnList = "code")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationStatus extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false)
    private String description;

    @Column(name = "is_final_state", nullable = false)
    private boolean isFinalState = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
