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
@Table(name = "conference")
public class ConferenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    @Column(name = "conference_room_id")
    private UUID conferenceRoomId;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
