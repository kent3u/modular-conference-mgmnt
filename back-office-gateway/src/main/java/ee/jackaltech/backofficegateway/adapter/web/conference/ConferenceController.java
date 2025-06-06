package ee.jackaltech.backofficegateway.adapter.web.conference;

import ee.jackaltech.conferenceplatform.appdomain.conference.*;
import ee.jackaltech.conferenceplatform.appdomain.feedback.GetConferenceFeedback;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conference")
class ConferenceController {

    private final CreateConference createConference;
    private final CancelConference cancelConference;
    private final GetConferenceAvailability getConferenceAvailability;
    private final ChangeConference changeConference;
    private final GetConferenceFeedback getConferenceFeedback;

    @PostMapping
    public void createConference(@RequestBody ConferenceCreationDto conferenceCreationDto) {
        createConference.execute(CreateConference.Request.builder()
                .conferenceRoomId(conferenceCreationDto.getConferenceRoomId())
                .conferenceStartTime(conferenceCreationDto.getConferenceStartTime())
                .conferenceEndTime(conferenceCreationDto.getConferenceEndTime())
                .build());
    }

    @PutMapping("/{conferenceId}")
    public void changeConference(@PathVariable UUID conferenceId, @RequestBody ConferenceChangeDto conferenceChangeDto) {
        changeConference.execute(ChangeConference.Request.builder()
                .conferenceId(conferenceId)
                .conferenceRoomId(conferenceChangeDto.getConferenceRoomId())
                .conferenceStartTime(conferenceChangeDto.getConferenceStartTime())
                .conferenceEndTime(conferenceChangeDto.getConferenceEndTime())
                .build());
    }

    @DeleteMapping("/{conferenceId}")
    public void cancelConference(@PathVariable UUID conferenceId) {
        cancelConference.execute(CancelConference.Request.of(conferenceId));
    }

    @GetMapping("/{conferenceId}/availability")
    public ConferenceAvailabilityDto getConferenceAvailability(@PathVariable UUID conferenceId) {
        var conferenceAvailability = getConferenceAvailability.execute(GetConferenceAvailability.Request.of(conferenceId));
        return ConferenceAvailabilityDto.builder()
                .conferenceId(conferenceAvailability.getConferenceId())
                .conferenceRoomCapacity(conferenceAvailability.getRoomCapacity())
                .numberOfParticipants(conferenceAvailability.getNumberOfParticipants())
                .build();
    }

    @GetMapping("/{conferenceId}/feedback")
    public ConferenceFeedbackDto getConferenceFeedback(@PathVariable UUID conferenceId) {
        var conferenceFeedback = getConferenceFeedback.execute(GetConferenceFeedback.Request.of(conferenceId));
        return ConferenceFeedbackDto.toDto(conferenceFeedback);
    }
}
