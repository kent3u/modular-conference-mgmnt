package ee.jackaltech.conferenceplatform.adapter.database.conference;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

interface RegistrationEntityRepository extends JpaRepository<RegistrationEntity, UUID> {

    Set<RegistrationEntity> findByConferenceId(UUID conferenceId);

    Long countByConferenceId(UUID conferenceId);

    Set<RegistrationEntity> findByConferenceIdIn(Set<UUID> conferenceIds);

    Optional<RegistrationEntity> findByConferenceParticipantCode(String conferenceParticipantCode);
}
