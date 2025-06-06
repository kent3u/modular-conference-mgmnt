package ee.jackaltech.conferenceplatform.appdomain.conference;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@FunctionalInterface
public interface ExistsConferenceOverlapInRoom {

    boolean execute(Request request);

    @Value
    @Builder
    class Request {
        UUID conferenceRoomId;
        LocalDateTime conferenceStartTime;
        LocalDateTime conferenceEndTime;
    }
}
