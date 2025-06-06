package ee.jackaltech.conferenceplatform.appdomain.conference;

import ee.jackaltech.conferenceplatform.appdomain.conference.exception.NoSuchConferenceException;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.GetConferenceRoomCapacity;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception.NoSuchConferenceRoomException;
import ee.jackaltech.conferenceplatform.appdomain.registration.FindConferenceRegistrationAmount;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetConferenceAvailability {

    private final GetConference getConference;
    private final GetConferenceRoomCapacity getConferenceRoomCapacity;
    private final FindConferenceRegistrationAmount findConferenceRegistrationAmount;

    public ConferenceAvailability execute(Request request) {
        Conference conference = getConference.execute(GetConference.Request.of(request.getConferenceId()))
                .orElseThrow(NoSuchConferenceException::new);
        Long conferenceRoomCapacity = getConferenceRoomCapacity.execute(GetConferenceRoomCapacity.Request.of(conference.getConferenceRoomId()))
                .orElseThrow(NoSuchConferenceRoomException::new);
        Long numberOfParticipants = findConferenceRegistrationAmount.execute(FindConferenceRegistrationAmount.Request.of(conference.getId()));
        return ConferenceAvailability.builder()
                .conferenceId(conference.getId())
                .roomCapacity(conferenceRoomCapacity)
                .numberOfParticipants(numberOfParticipants)
                .build();
    }

    @Value(staticConstructor = "of")
    public static class Request {
        UUID conferenceId;
    }

    @Value
    @Builder
    public static class ConferenceAvailability {
        UUID conferenceId;
        Long roomCapacity;
        Long numberOfParticipants;
    }
}
