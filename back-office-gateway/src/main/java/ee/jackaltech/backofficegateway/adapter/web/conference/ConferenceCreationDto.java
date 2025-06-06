package ee.jackaltech.backofficegateway.adapter.web.conference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ConferenceCreationDto {

    private UUID conferenceRoomId;
    private LocalDateTime conferenceStartTime;
    private LocalDateTime conferenceEndTime;
}
