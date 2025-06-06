package ee.jackaltech.conferenceplatform.appdomain.registration;

import ee.jackaltech.conferenceplatform.appdomain.registration.Registration.ConferenceParticipantCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancelRegistration {

    private final DeleteRegistration deleteRegistration;

    public void execute(Request request) {
        deleteRegistration.execute(DeleteRegistration.Request.of(request.getConferenceParticipantCode()));
    }

    @Value(staticConstructor = "of")
    public static class Request {
        ConferenceParticipantCode conferenceParticipantCode;
    }
}
