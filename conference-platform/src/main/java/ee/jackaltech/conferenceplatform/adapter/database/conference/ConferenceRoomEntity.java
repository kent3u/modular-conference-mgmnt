package ee.jackaltech.conferenceplatform.adapter.database.conference;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "conference_room")
public class ConferenceRoomEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ConferenceRoomStatus status;
    @Column(name = "location")
    private String location;
    @Column(name = "capacity")
    private Long capacity;

    public enum ConferenceRoomStatus {
        UNDER_CONSTRUCTION, AVAILABLE
    }
}
