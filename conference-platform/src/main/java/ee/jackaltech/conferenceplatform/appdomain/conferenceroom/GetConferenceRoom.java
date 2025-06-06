package ee.jackaltech.conferenceplatform.appdomain.conferenceroom;

import lombok.Value;

import java.util.Optional;
import java.util.UUID;

@FunctionalInterface
public interface GetConferenceRoom {

    Optional<ConferenceRoom> execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        UUID conferenceRoomId;
    }
}
