package ee.jackaltech.conferenceplatform.appdomain.feedback;

import lombok.Value;

import java.util.Set;
import java.util.UUID;

@FunctionalInterface
public interface FindFeedbacksByConferenceId {

    Set<Feedback> execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        UUID conferenceId;
    }
}
