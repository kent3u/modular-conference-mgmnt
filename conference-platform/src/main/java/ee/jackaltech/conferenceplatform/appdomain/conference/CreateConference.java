package ee.jackaltech.conferenceplatform.appdomain.conference;

import ee.jackaltech.conferenceplatform.adapter.database.conference.ConferenceRoomEntity.ConferenceRoomStatus;
import ee.jackaltech.conferenceplatform.appdomain.conference.exception.ConferenceOverlapException;
import ee.jackaltech.conferenceplatform.appdomain.conference.exception.ConferenceTimeException;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.ConferenceRoom;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.GetConferenceRoom;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception.ConferenceRoomUnderConstructionException;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception.NoSuchConferenceRoomException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateConference {

    private final SaveConference saveConference;
    private final GetConferenceRoom getConferenceRoom;
    private final ExistsConferenceOverlapInRoom existsConferenceOverlapInRoom;
    private final Clock clock;

    public void execute(Request request) {
        if (request.getConferenceStartTime().isAfter(request.getConferenceEndTime()) || request.getConferenceStartTime().isBefore(LocalDateTime.now(clock))) {
            throw new ConferenceTimeException();
        }

        ConferenceRoom conferenceRoom = getConferenceRoom.execute(GetConferenceRoom.Request.of(request.getConferenceRoomId()))
                .orElseThrow(NoSuchConferenceRoomException::new);
        if (Objects.equals(ConferenceRoomStatus.UNDER_CONSTRUCTION, conferenceRoom.getStatus())) {
            throw new ConferenceRoomUnderConstructionException();
        }

        if (existsConferenceOverlapInRoom.execute(ExistsConferenceOverlapInRoom.Request.builder()
                .conferenceRoomId(conferenceRoom.getId())
                .conferenceStartTime(request.getConferenceStartTime())
                .conferenceEndTime(request.getConferenceEndTime())
                .build())) {
            throw new ConferenceOverlapException();
        }

        saveConference.execute(SaveConference.Request.of(Conference.builder()
                .id(UUID.randomUUID())
                .conferenceRoomId(conferenceRoom.getId())
                .startTime(request.getConferenceStartTime())
                .endTime(request.getConferenceEndTime())
                .build()));
    }

    @Value
    @Builder
    public static class Request {
        UUID conferenceRoomId;
        LocalDateTime conferenceStartTime;
        LocalDateTime conferenceEndTime;
    }
}
