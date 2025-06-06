package ee.jackaltech.conferenceplatform.appdomain.conference;

import lombok.Value;

@FunctionalInterface
public interface SaveConference {

    void execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        Conference conference;
    }
}
