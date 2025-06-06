package ee.jackaltech.conferencegateway.adapter.web.feedback;

import ee.jackaltech.conferenceplatform.appdomain.feedback.SendFeedback;
import ee.jackaltech.conferenceplatform.appdomain.registration.Registration.ConferenceParticipantCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
class FeedbackController {

    private final SendFeedback sendFeedback;

    @PostMapping
    public void sendFeedback(@RequestBody SendFeedbackDto sendFeedbackDto) {
        sendFeedback.execute(SendFeedback.Request.builder()
                .conferenceParticipantCode(ConferenceParticipantCode.of(sendFeedbackDto.getConferenceParticipantCode()))
                .feedback(sendFeedbackDto.getFeedback())
                .build());
    }
}
