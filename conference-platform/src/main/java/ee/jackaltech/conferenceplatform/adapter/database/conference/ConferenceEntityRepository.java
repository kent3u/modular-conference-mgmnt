package ee.jackaltech.conferenceplatform.adapter.database.conference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

interface ConferenceEntityRepository extends JpaRepository<ConferenceEntity, UUID>, JpaSpecificationExecutor<ConferenceEntity> {

    Optional<UUID> findConferenceRoomIdById(UUID conferenceId);
}
