package ee.jackaltech.conferenceplatform.appdomain.conferenceroom;

import lombok.Value;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@FunctionalInterface
public interface FindConferenceRooms {

    Map<UUID, ConferenceRoom> execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        Set<UUID> conferenceRoomIds;
    }
}
