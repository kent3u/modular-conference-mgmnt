package ee.jackaltech.conferenceplatform.adapter.database.feedback;

import ee.jackaltech.conferenceplatform.appdomain.feedback.Feedback;
import ee.jackaltech.conferenceplatform.appdomain.feedback.FindFeedbacksByConferenceId;
import ee.jackaltech.conferenceplatform.appdomain.feedback.SaveFeedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FeedbackRepositoryAdapter implements FindFeedbacksByConferenceId, SaveFeedback {

    private final FeedbackEntityRepository feedbackEntityRepository;

    @Override
    public Set<Feedback> execute(FindFeedbacksByConferenceId.Request request) {
        return feedbackEntityRepository.findByConferenceId(request.getConferenceId()).stream()
                .map(this::toModel)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public void execute(SaveFeedback.Request request) {
        feedbackEntityRepository.save(toEntity(request.getFeedback()));
    }

    private FeedbackEntity toEntity(Feedback feedback) {
        return FeedbackEntity.builder()
                .conferenceId(feedback.getConferenceId())
                .participantId(feedback.getParticipantId())
                .feedback(feedback.getFeedback())
                .build();
    }

    private Feedback toModel(FeedbackEntity feedbackEntity) {
        return Feedback.builder()
                .id(feedbackEntity.getId())
                .conferenceId(feedbackEntity.getConferenceId())
                .participantId(feedbackEntity.getParticipantId())
                .feedback(feedbackEntity.getFeedback())
                .build();
    }
}
