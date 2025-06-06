package ee.jackaltech.conferenceplatform.appdomain.conferenceroom;

import lombok.Value;

@FunctionalInterface
public interface SaveConferenceRoom {

    void execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        ConferenceRoom conferenceRoom;
    }
}
