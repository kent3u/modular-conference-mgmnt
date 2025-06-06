package ee.jackaltech.backofficegateway.adapter.web.conferenceroom;

import ee.jackaltech.conferenceplatform.adapter.database.conference.ConferenceRoomEntity.ConferenceRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ConferenceRoomChangeDto {

    private Long conferenceRoomCapacity;
    private ConferenceRoomStatus conferenceRoomStatus;
}
