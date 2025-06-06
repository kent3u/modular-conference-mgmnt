package ee.jackaltech.conferenceplatform.appdomain.conferenceroom;

import lombok.Value;

import java.util.Optional;
import java.util.UUID;

@FunctionalInterface
public interface GetConferenceRoomCapacity {

    Optional<Long> execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        UUID conferenceRoomId;
    }
}
