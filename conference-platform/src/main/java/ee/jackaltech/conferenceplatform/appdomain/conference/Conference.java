package ee.jackaltech.conferenceplatform.appdomain.conference;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Conference {

    UUID id;
    UUID conferenceRoomId;
    LocalDateTime startTime;
    LocalDateTime endTime;
}
