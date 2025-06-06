package ee.jackaltech.conferenceplatform.adapter.database.conference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted_at is NULL")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "registration")
public class RegistrationEntity {

    @Id
    @Builder.Default
    @EqualsAndHashCode.Include
    private UUID id = UUID.randomUUID();
    @Column(name = "conference_participant_code")
    private String conferenceParticipantCode;
    @Column(name = "conference_id")
    private UUID conferenceId;
    @Column(name = "participant_id")
    private UUID participantId;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
