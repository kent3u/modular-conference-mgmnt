package ee.jackaltech.conferenceplatform.adapter.database.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

interface FeedbackEntityRepository extends JpaRepository<FeedbackEntity, UUID> {

    Set<FeedbackEntity> findByConferenceId(UUID conferenceId);
}
