package br.com.pauloultra.desafioluizalabs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "communication_schedules")
@Table(indexes = {
        @Index(name = "IDX_GUID_COMMUNICATION_SCHEDULES", columnList = "guid"),
        @Index(name = "IDX_SCHEDULED_DATETIME", columnList = "scheduled_date_time"),
        @Index(name = "IDX_STATUS", columnList = "status_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationSchedule extends BaseEntity {

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
    private CommunicationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private CommunicationStatus status;
}
