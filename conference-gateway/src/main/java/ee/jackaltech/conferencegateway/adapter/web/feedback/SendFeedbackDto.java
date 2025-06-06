package ee.jackaltech.conferencegateway.adapter.web.feedback;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class SendFeedbackDto {

    String conferenceParticipantCode;
    String feedback;
}
