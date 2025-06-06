package ee.jackaltech.backofficegateway.adapter.web.conference;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
class ConferenceAvailabilityDto {

    UUID conferenceId;
    Long conferenceRoomCapacity;
    Long numberOfParticipants;
}
