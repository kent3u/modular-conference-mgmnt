package ee.jackaltech.conferenceplatform.appdomain.conferenceroom;

import ee.jackaltech.conferenceplatform.adapter.database.conference.ConferenceRoomEntity.ConferenceRoomStatus;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class ConferenceRoom {

    UUID id;
    String name;
    ConferenceRoomStatus status;
    String location;
    Long capacity;
}
