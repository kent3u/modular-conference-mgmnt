package ee.jackaltech.conferenceplatform.appdomain.conference;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

@FunctionalInterface
public interface FindConferencesForTimePeriod {

    Set<Conference> execute(Request request);

    @Value
    @Builder
    class Request {
        LocalDateTime from;
        LocalDateTime until;
    }
}
