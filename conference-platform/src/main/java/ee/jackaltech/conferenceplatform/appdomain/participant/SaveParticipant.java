package ee.jackaltech.conferenceplatform.appdomain.participant;

import lombok.Value;

@FunctionalInterface
public interface SaveParticipant {

    void execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        Participant participant;
    }
}
