package ee.jackaltech.conferenceplatform.appdomain.conferenceroom;

import ee.jackaltech.conferenceplatform.adapter.database.conference.ConferenceRoomEntity.ConferenceRoomStatus;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateConferenceRoom {

    private final SaveConferenceRoom saveConferenceRoom;

    public Response execute(Request request) {
        UUID newId = UUID.randomUUID();

        // todo validation e.g dont create too big rooms, location follows certain form etc

        ConferenceRoom conferenceRoom = toDomain(newId, request);
        saveConferenceRoom.execute(SaveConferenceRoom.Request.of(conferenceRoom));

        return Response.of(newId);
    }

    private ConferenceRoom toDomain(UUID newId, Request request) {
        return ConferenceRoom.builder()
                .id(newId)
                .name(request.getRoomName())
                .status(request.getRoomStatus())
                .location(request.getRoomLocation())
                .capacity(request.getRoomCapacity())
                .build();
    }

    @Value
    @Builder
    public static class Request {
        String roomName;
        ConferenceRoomStatus roomStatus;
        String roomLocation;
        Long roomCapacity;
    }

    @Value(staticConstructor = "of")
    public static class Response {
        UUID conferenceRoomId;
    }
}
