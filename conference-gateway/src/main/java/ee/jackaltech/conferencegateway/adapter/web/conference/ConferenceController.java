package ee.jackaltech.conferencegateway.adapter.web.conference;

import ee.jackaltech.conferenceplatform.appdomain.conference.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conference")
class ConferenceController {

    private final FindAvailableConferences findAvailableConferences;

    @GetMapping
    public Set<ConferenceDetailsDto> findAvailableConferences(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTimeFrom,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTimeUntil) {
        var availableConferences = findAvailableConferences.execute(FindAvailableConferences.Request.builder()
                .conferenceStartTimeFrom(startTimeFrom)
                .conferenceStartTimeUntil(startTimeUntil)
                .build());

        return availableConferences.stream()
                .map(availableConference -> ConferenceDetailsDto.builder()
                        .conferenceId(availableConference.getConferenceId())
                        .conferenceStartTime(availableConference.getConferenceStartTime())
                        .conferenceEndTime(availableConference.getConferenceEndTime())
                        .conferenceLocation(availableConference.getConferenceLocation())
                        .conferenceCapacity(availableConference.getConferenceCapacity())
                        .numberOfRegistrations(availableConference.getNumberOfRegistrations())
                        .build())
                .collect(Collectors.toUnmodifiableSet());
    }
}
