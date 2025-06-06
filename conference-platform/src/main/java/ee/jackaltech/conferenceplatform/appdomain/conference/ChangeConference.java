package ee.jackaltech.conferenceplatform.appdomain.conference;

import ee.jackaltech.conferenceplatform.appdomain.conference.exception.ConferenceOverlapException;
import ee.jackaltech.conferenceplatform.appdomain.conference.exception.ConferenceTimeException;
import ee.jackaltech.conferenceplatform.appdomain.conference.exception.NoSuchConferenceException;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.ConferenceRoom;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.GetConferenceRoom;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception.ConferenceRoomCapacityException;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception.ConferenceRoomUnderConstructionException;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception.NoSuchConferenceRoomException;
import ee.jackaltech.conferenceplatform.appdomain.registration.FindConferenceRegistrationAmount;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static ee.jackaltech.conferenceplatform.adapter.database.conference.ConferenceRoomEntity.ConferenceRoomStatus.UNDER_CONSTRUCTION;

@Component
@RequiredArgsConstructor
public class ChangeConference {

    private final GetConference getConference;
    private final SaveConference saveConference;
    private final GetConferenceRoom getConferenceRoom;
    private final ExistsConferenceOverlapInRoom existsConferenceOverlapInRoom;
    private final FindConferenceRegistrationAmount findConferenceRegistrationAmount;
    private final Clock clock;

    public void execute(Request request) {
        Conference conference = getConference.execute(GetConference.Request.of(request.getConferenceId()))
                .orElseThrow(NoSuchConferenceException::new);

        validateRequest(request);

        Conference changedConference = conference.toBuilder()
                .startTime(request.getConferenceStartTime())
                .endTime(request.getConferenceEndTime())
                .conferenceRoomId(request.getConferenceRoomId())
                .build();

        saveConference.execute(SaveConference.Request.of(changedConference));
    }

    private void validateRequest(Request request) {
        // todo check null values if needed, currently not expecting null values
        if (request.getConferenceStartTime().isAfter(request.getConferenceEndTime()) || request.getConferenceStartTime().isBefore(LocalDateTime.now(clock))) {
            throw new ConferenceTimeException();
        }
        ConferenceRoom conferenceRoom = getConferenceRoom.execute(GetConferenceRoom.Request.of(request.getConferenceRoomId()))
                .orElseThrow(NoSuchConferenceRoomException::new);
        if (Objects.equals(UNDER_CONSTRUCTION, conferenceRoom.getStatus())) {
            throw new ConferenceRoomUnderConstructionException();
        }
        if (existsConferenceOverlapInRoom.execute(ExistsConferenceOverlapInRoom.Request.builder()
                .conferenceRoomId(request.getConferenceRoomId())
                .conferenceStartTime(request.getConferenceStartTime())
                .conferenceEndTime(request.getConferenceEndTime())
                .build())) {
            throw new ConferenceOverlapException();
        }
        if (findConferenceRegistrationAmount.execute(FindConferenceRegistrationAmount.Request.of(request.getConferenceId())) > conferenceRoom.getCapacity()) {
            throw new ConferenceRoomCapacityException();
        }
    }

    @Value
    @Builder
    public static class Request {
        UUID conferenceId;
        LocalDateTime conferenceStartTime;
        LocalDateTime conferenceEndTime;
        UUID conferenceRoomId;
    }
}
