package br.com.pauloultra.desafioluizalabs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "communication_schedules")
@Table(indexes = {
        @Index(name = "IDX_GUID_COMMUNICATION_SCHEDULES", columnList = "guid"),
        @Index(name = "IDX_SCHEDULED_DATETIME", columnList = "scheduled_date_time"),
        @Index(name = "IDX_STATUS", columnList = "status_id")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationSchedule {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID guid;

    @Column(nullable = false)
    private LocalDateTime scheduledDateTime;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    @JsonIgnore
    private CommunicationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    @JsonIgnore
    private CommunicationStatus status;

    @Version
    @Column(nullable = false)
    private Long version = 0L;
}
