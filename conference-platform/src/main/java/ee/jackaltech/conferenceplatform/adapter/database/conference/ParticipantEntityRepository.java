package ee.jackaltech.conferenceplatform.adapter.database.conference;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

interface ParticipantEntityRepository extends JpaRepository<ParticipantEntity, UUID> {

    Set<ParticipantEntity> findByIdIn(Set<UUID> ids);
}
