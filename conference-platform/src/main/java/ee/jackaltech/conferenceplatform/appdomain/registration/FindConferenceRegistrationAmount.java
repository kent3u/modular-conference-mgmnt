package ee.jackaltech.conferenceplatform.appdomain.registration;

import lombok.Value;

import java.util.UUID;

@FunctionalInterface
public interface FindConferenceRegistrationAmount {

    Long execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        UUID conferenceId;
    }
}
