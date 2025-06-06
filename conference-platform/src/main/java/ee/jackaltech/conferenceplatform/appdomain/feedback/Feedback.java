package ee.jackaltech.conferenceplatform.appdomain.feedback;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class Feedback {

    UUID id;
    UUID conferenceId;
    UUID participantId;
    String feedback;
}
