package ee.jackaltech.conferenceplatform.appdomain.participant;

import lombok.Value;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@FunctionalInterface
public interface FindParticipantNamesByIds {

    Map<UUID, ParticipantName> execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        Set<UUID> participantIds;
    }

    @Value(staticConstructor = "of")
    class ParticipantName {
        String firstName;
        String lastName;
    }
}
