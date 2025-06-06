package ee.jackaltech.backofficegateway.adapter.web.conferenceroom;

import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.ChangeConferenceRoom;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.CreateConferenceRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conference-room")
class ConferenceRoomController {

    private final CreateConferenceRoom createConferenceRoom;
    private final ChangeConferenceRoom changeConferenceRoom;

    @PostMapping
    public UUID createConferenceRoom(@RequestBody ConferenceRoomCreationDto conferenceRoomCreationDto) {
        var response = createConferenceRoom.execute(CreateConferenceRoom.Request.builder()
                .roomName(conferenceRoomCreationDto.getRoomName())
                .roomStatus(conferenceRoomCreationDto.getRoomStatus())
                .roomLocation(conferenceRoomCreationDto.getRoomLocation())
                .roomCapacity(conferenceRoomCreationDto.getRoomCapacity())
                .build());
        return response.getConferenceRoomId();
    }

    @PutMapping("/{conferenceRoomId}")
    public void changeConferenceRoom(@PathVariable UUID conferenceRoomId, @RequestBody ConferenceRoomChangeDto conferenceRoomChangeDto) {
        changeConferenceRoom.execute(ChangeConferenceRoom.Request.builder()
                .conferenceRoomId(conferenceRoomId)
                .conferenceRoomCapacity(conferenceRoomChangeDto.getConferenceRoomCapacity())
                .conferenceRoomStatus(conferenceRoomChangeDto.getConferenceRoomStatus())
                .build());
    }
}
