package ee.jackaltech.conferenceplatform.appdomain.feedback;

import lombok.Value;

@FunctionalInterface
public interface SaveFeedback {

    void execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        Feedback feedback;
    }
}
