package ee.jackaltech.conferenceplatform.appdomain.conference;
import ee.jackaltech.conferenceplatform.appdomain.conference.exception.CancellingActiveConferenceException;
import ee.jackaltech.conferenceplatform.appdomain.conference.exception.NoSuchConferenceException;
import ee.jackaltech.conferenceplatform.appdomain.registration.DeleteRegistrationsByConferenceId;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
public class CancelConference {

    private final GetConference getConference;
    private final DeleteConference deleteConference;
    private final DeleteRegistrationsByConferenceId deleteRegistrationsByConferenceId;
    private final Clock clock;

    public void execute(Request request) {
        Conference conference = getConference.execute(GetConference.Request.of(request.getConferenceId()))
                .orElseThrow(NoSuchConferenceException::new);
        // todo should check actor ownership of given conference

        if (conference.getStartTime().isBefore(LocalDateTime.now(clock))) {
            throw new CancellingActiveConferenceException();
        }

        deleteRegistrationsByConferenceId.execute(DeleteRegistrationsByConferenceId.Request.of(request.getConferenceId()));
        deleteConference.execute(DeleteConference.Request.of(conference));
    }

    @Value(staticConstructor = "of")
    public static class Request {
        UUID conferenceId;
    }
}
