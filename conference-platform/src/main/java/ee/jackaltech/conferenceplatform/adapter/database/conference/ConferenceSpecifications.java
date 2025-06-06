package ee.jackaltech.conferenceplatform.adapter.database.conference;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConferenceSpecifications {

    private final Clock clock;

    public Specification<ConferenceEntity> conferenceRoomIdEquals(UUID conferenceRoomId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("conferenceRoomId"), conferenceRoomId);
    }

    public Specification<ConferenceEntity> lastingInFuture() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), LocalDateTime.now(clock));
    }

    public Specification<ConferenceEntity> overlapsWithTime(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.or(
                    criteriaBuilder.and(
                            criteriaBuilder.lessThanOrEqualTo(root.get("startTime"), end),
                            criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), start)
                    ),
                    criteriaBuilder.and(
                            criteriaBuilder.lessThanOrEqualTo(root.get("endTime"), end),
                            criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), start)
                    ),
                    criteriaBuilder.and(
                            criteriaBuilder.lessThanOrEqualTo(root.get("startTime"), start),
                            criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), end)
                    )
            );
    }

    public Specification<ConferenceEntity> minNumberOfRegistrations(Long minRegistrations) {
        // todo indexes!
        // todo for the future, after analysing postgresql performance, join might be necessary here instead of subquery
        return (conferenceRoot, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<RegistrationEntity> registrationRoot = subquery.from(RegistrationEntity.class);

            subquery.select(criteriaBuilder.count(registrationRoot))
                    .where(criteriaBuilder.equal(registrationRoot.get("conferenceId"), conferenceRoot.get("id")));

            return criteriaBuilder.greaterThan(subquery, minRegistrations);
        };
    }

    public Specification<ConferenceEntity> startTimeBetween(LocalDateTime from, LocalDateTime until) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("startTime"), from, until);
    }
}
