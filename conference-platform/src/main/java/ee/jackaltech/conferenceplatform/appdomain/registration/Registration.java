package ee.jackaltech.conferenceplatform.appdomain.registration;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class Registration {

    ConferenceParticipantCode conferenceParticipantCode;
    UUID conferenceId;
    UUID participantId;

    @Value(staticConstructor = "of")
    public static class ConferenceParticipantCode {
        String code;
    }
}
