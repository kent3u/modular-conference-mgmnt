package ee.jackaltech.conferenceplatform.appdomain.registration;

import ee.jackaltech.conferenceplatform.appdomain.registration.Registration.ConferenceParticipantCode;
import lombok.Value;

import java.util.Optional;

@FunctionalInterface
public interface GetRegistrationByParticipantCode {

    Optional<Registration> execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        ConferenceParticipantCode conferenceParticipantCode;
    }
}
