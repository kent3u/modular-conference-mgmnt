package ee.jackaltech.conferenceplatform.appdomain.feedback;

import ee.jackaltech.conferenceplatform.appdomain.registration.GetRegistrationByParticipantCode;
import ee.jackaltech.conferenceplatform.appdomain.registration.Registration;
import ee.jackaltech.conferenceplatform.appdomain.registration.Registration.ConferenceParticipantCode;
import ee.jackaltech.conferenceplatform.appdomain.registration.exception.NoSuchRegistrationException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendFeedback {

    private final SaveFeedback saveFeedback;
    private final GetRegistrationByParticipantCode getRegistrationByParticipantCode;

    public void execute(Request request) {
        Registration registration = getRegistrationByParticipantCode.execute(GetRegistrationByParticipantCode.Request.of(request.getConferenceParticipantCode()))
                .orElseThrow(NoSuchRegistrationException::new);
        saveFeedback.execute(SaveFeedback.Request.of(Feedback.builder()
                .conferenceId(registration.getConferenceId())
                .participantId(registration.getParticipantId())
                .feedback(request.getFeedback())
                .build()));
    }

    @Value
    @Builder
    public static class Request {
        ConferenceParticipantCode conferenceParticipantCode;
        String feedback;
    }
}
