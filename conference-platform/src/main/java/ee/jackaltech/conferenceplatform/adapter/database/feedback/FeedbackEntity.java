package ee.jackaltech.conferenceplatform.adapter.database.feedback;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "feedback")
public class FeedbackEntity {

    @Id
    @Builder.Default
    @EqualsAndHashCode.Include
    private UUID id = UUID.randomUUID();
    @Column(name = "conference_id")
    private UUID conferenceId;
    @Column(name = "participant_id")
    private UUID participantId;
    @Column(name = "feedback")
    private String feedback;
}
