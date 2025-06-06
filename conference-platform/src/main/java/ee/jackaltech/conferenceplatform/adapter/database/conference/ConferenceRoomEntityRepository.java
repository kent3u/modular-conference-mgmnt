package ee.jackaltech.conferenceplatform.adapter.database.conference;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface ConferenceRoomEntityRepository extends JpaRepository<ConferenceRoomEntity, UUID> {

    Optional<ConferenceRoomEntity> findCapacityById(UUID conferenceRoomId);
}
