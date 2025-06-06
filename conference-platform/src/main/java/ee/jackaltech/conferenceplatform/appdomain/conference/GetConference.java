package ee.jackaltech.conferenceplatform.appdomain.conference;

import lombok.Value;

import java.util.Optional;
import java.util.UUID;

@FunctionalInterface
public interface GetConference {

    Optional<Conference> execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        UUID conferenceId;
    }
}
