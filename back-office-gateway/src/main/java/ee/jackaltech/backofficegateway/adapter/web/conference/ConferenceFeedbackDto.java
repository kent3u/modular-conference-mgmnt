package ee.jackaltech.backofficegateway.adapter.web.conference;

import ee.jackaltech.conferenceplatform.appdomain.feedback.GetConferenceFeedback.ConferenceFeedback;
import lombok.Builder;
import lombok.Value;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Value
@Builder
class ConferenceFeedbackDto {

    UUID conferenceId;
    Set<ParticipantFeedbackDto> participantFeedbacks;

    @Value
    @Builder
    private static class ParticipantFeedbackDto {
        String participantName;
        String feedback;
    }

    public static ConferenceFeedbackDto toDto(ConferenceFeedback conferenceFeedback) {
        return ConferenceFeedbackDto.builder()
                .conferenceId(conferenceFeedback.getConferenceId())
                .participantFeedbacks(conferenceFeedback.getParticipantFeedbacks().stream()
                        .map(participantFeedback -> ParticipantFeedbackDto.builder()
                                .participantName(participantFeedback.getParticipantName())
                                .feedback(participantFeedback.getFeedback())
                                .build())
                        .collect(Collectors.toUnmodifiableSet()))
                .build();
    }
}
