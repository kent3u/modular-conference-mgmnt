package ee.jackaltech.conferenceplatform.appdomain.registration;

import ee.jackaltech.conferenceplatform.appdomain.registration.Registration.ConferenceParticipantCode;
import lombok.Value;

@FunctionalInterface
public interface DeleteRegistration {

    void execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        ConferenceParticipantCode conferenceParticipantCode;
    }
}
