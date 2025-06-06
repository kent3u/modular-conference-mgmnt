package ee.jackaltech.conferenceplatform.appdomain.registration;

import lombok.Value;

@FunctionalInterface
public interface SaveRegistration {

    void execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        Registration registration;
    }
}
