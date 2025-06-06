package ee.jackaltech.conferenceplatform.appdomain.conference;

import lombok.Value;

import java.util.UUID;

@FunctionalInterface
public interface ExistsConferenceInRoomInTheFuture {

    boolean execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        UUID conferenceRoomId;
    }

}
