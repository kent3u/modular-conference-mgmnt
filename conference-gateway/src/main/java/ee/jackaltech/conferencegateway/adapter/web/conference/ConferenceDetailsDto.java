package ee.jackaltech.conferencegateway.adapter.web.conference;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
class ConferenceDetailsDto {

    UUID conferenceId;
    LocalDateTime conferenceStartTime;
    LocalDateTime conferenceEndTime;
    String conferenceLocation;
    Long conferenceCapacity;
    Long numberOfRegistrations;
}
