package ee.jackaltech.conferenceplatform.adapter.database.conference;

import ee.jackaltech.conferenceplatform.appdomain.participant.FindParticipantNamesByIds;
import ee.jackaltech.conferenceplatform.appdomain.participant.Participant;
import ee.jackaltech.conferenceplatform.appdomain.participant.SaveParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public class ParticipantRepositoryAdapter implements FindParticipantNamesByIds, SaveParticipant {

    private final ParticipantEntityRepository participantEntityRepository;

    @Override
    public Map<UUID, ParticipantName> execute(FindParticipantNamesByIds.Request request) {
        if (request.getParticipantIds().isEmpty()) {
            return Map.of();
        }
        return participantEntityRepository.findByIdIn(request.getParticipantIds()).stream()
                .collect(toMap(ParticipantEntity::getId, participant -> ParticipantName.of(participant.getFirstName(), participant.getLastName())));
    }

    @Override
    public void execute(SaveParticipant.Request request) {
        participantEntityRepository.save(toEntity(request.getParticipant()));
    }

    private ParticipantEntity toEntity(Participant participant) {
        return ParticipantEntity.builder()
                .id(participant.getId())
                .firstName(participant.getFirstName())
                .lastName(participant.getLastName())
                .email(participant.getEmail())
                .gender(participant.getGender())
                .dateOfBirth(participant.getDateOfBirth())
                .build();
    }
}
