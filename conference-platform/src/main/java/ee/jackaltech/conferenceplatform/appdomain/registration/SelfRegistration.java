package ee.jackaltech.conferenceplatform.appdomain.registration;

import ee.jackaltech.conferenceplatform.adapter.database.conference.ParticipantEntity.Gender;
import ee.jackaltech.conferenceplatform.appdomain.conference.Conference;
import ee.jackaltech.conferenceplatform.appdomain.conference.GetConference;
import ee.jackaltech.conferenceplatform.appdomain.conference.exception.NoSuchConferenceException;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.GetConferenceRoomCapacity;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception.ConferenceRoomCapacityException;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception.NoSuchConferenceRoomException;
import ee.jackaltech.conferenceplatform.appdomain.participant.Participant;
import ee.jackaltech.conferenceplatform.appdomain.participant.SaveParticipant;
import ee.jackaltech.conferenceplatform.appdomain.registration.Registration.ConferenceParticipantCode;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
public class SelfRegistration {

    private final GetConference getConference;
    private final SaveRegistration saveRegistration;
    private final SaveParticipant saveParticipant;
    private final GetConferenceRoomCapacity getConferenceRoomCapacity;
    private final FindConferenceRegistrationAmount findConferenceRegistrationAmount;

    public ConferenceParticipantCode execute(Request request) {
        validateRegistration(request);

        UUID participantId = UUID.randomUUID();
        saveParticipant(request, participantId);

        // todo should check for collisions and handle them
        ConferenceParticipantCode conferenceParticipantCode = ConferenceParticipantCode.of(ConferenceParticipantCodeGenerator.generateCode());
        saveRegistration(request, participantId, conferenceParticipantCode);

        return conferenceParticipantCode;
    }

    private void saveRegistration(Request request, UUID participantId, ConferenceParticipantCode participantCode) {
        saveRegistration.execute(SaveRegistration.Request.of(Registration.builder()
                .conferenceId(request.getConferenceId())
                .participantId(participantId)
                .conferenceParticipantCode(participantCode)
                .build()));
    }

    private void saveParticipant(Request request, UUID participantId) {
        Participant participant = Participant.builder()
                .id(participantId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .build();
        saveParticipant.execute(SaveParticipant.Request.of(participant));
    }

    private void validateRegistration(Request request) {
        // todo should also validate participant fields
        Conference conference = getConference.execute(GetConference.Request.of(request.getConferenceId()))
                .orElseThrow(NoSuchConferenceException::new);
        Long conferenceRoomCapacity = getConferenceRoomCapacity.execute(GetConferenceRoomCapacity.Request.of(conference.getConferenceRoomId()))
                .orElseThrow(NoSuchConferenceRoomException::new);
        Long amountOfRegistrations = findConferenceRegistrationAmount.execute(FindConferenceRegistrationAmount.Request.of(conference.getId()));

        if (conferenceRoomCapacity - amountOfRegistrations < 1) {
            throw new ConferenceRoomCapacityException();
        }
    }

    @Value
    @Builder
    public static class Request {
        UUID conferenceId;
        String firstName;
        String lastName;
        Gender gender;
        String email;
        LocalDate dateOfBirth;
    }
}
