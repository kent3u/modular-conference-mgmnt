package ee.jackaltech.conferenceplatform.appdomain.registration;

import lombok.Value;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@FunctionalInterface
public interface FindRegistrationsAmountsByConferenceIds {

    Map<UUID, Long> execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        Set<UUID> conferenceIds;
    }
}
