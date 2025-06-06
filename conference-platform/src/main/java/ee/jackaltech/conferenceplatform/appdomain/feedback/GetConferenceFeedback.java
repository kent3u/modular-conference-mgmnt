package ee.jackaltech.conferenceplatform.appdomain.feedback;

import ee.jackaltech.conferenceplatform.appdomain.conference.Conference;
import ee.jackaltech.conferenceplatform.appdomain.conference.GetConference;
import ee.jackaltech.conferenceplatform.appdomain.conference.exception.NoSuchConferenceException;
import ee.jackaltech.conferenceplatform.appdomain.participant.FindParticipantNamesByIds;
import ee.jackaltech.conferenceplatform.appdomain.participant.FindParticipantNamesByIds.ParticipantName;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetConferenceFeedback {

    private final GetConference getConference;
    private final FindFeedbacksByConferenceId findFeedbacksByConferenceId;
    private final FindParticipantNamesByIds findParticipantNamesByIds;

    public ConferenceFeedback execute(Request request) {
        Conference conference = getConference.execute(GetConference.Request.of(request.getConferenceId()))
                .orElseThrow(NoSuchConferenceException::new);

        Set<Feedback> feedbacks = findFeedbacksByConferenceId.execute(FindFeedbacksByConferenceId.Request.of(conference.getId()));
        Set<UUID> participantIds = feedbacks.stream().map(Feedback::getParticipantId).collect(Collectors.toUnmodifiableSet());
        Map<UUID, ParticipantName> participantNames = findParticipantNamesByIds.execute(FindParticipantNamesByIds.Request.of(participantIds));

        return ConferenceFeedback.builder()
                .conferenceId(conference.getId())
                .participantFeedbacks(feedbacks.stream()
                        .map(feedback -> ParticipantFeedback.of(formatName(participantNames.get(feedback.getParticipantId())), feedback.getFeedback()))
                        .collect(Collectors.toUnmodifiableSet()))
                .build();
    }

    private String formatName(ParticipantName participantName) {
        return "%s %s".formatted(participantName.getFirstName(),
                participantName.getLastName().charAt(0) + "*".repeat(participantName.getLastName().length() - 1));
    }

    @Value(staticConstructor = "of")
    public static class Request {
        UUID conferenceId;
    }

    @Value
    @Builder
    public static class ConferenceFeedback {
        UUID conferenceId;
        Set<ParticipantFeedback> participantFeedbacks;
    }

    @Value(staticConstructor = "of")
    public static class ParticipantFeedback {
        String participantName;
        String feedback;
    }
}
