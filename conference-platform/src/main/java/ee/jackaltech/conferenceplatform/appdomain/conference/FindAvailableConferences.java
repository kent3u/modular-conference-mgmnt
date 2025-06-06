package ee.jackaltech.conferenceplatform.appdomain.conference;

import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.ConferenceRoom;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.FindConferenceRooms;
import ee.jackaltech.conferenceplatform.appdomain.registration.FindRegistrationsAmountsByConferenceIds;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FindAvailableConferences {

    private final FindConferencesForTimePeriod findConferencesForTimePeriod;
    private final FindRegistrationsAmountsByConferenceIds findRegistrationsAmountsByConferenceIds;
    private final FindConferenceRooms findConferenceRooms;

    public Set<ConferenceDetails> execute(Request request) {
        Set<Conference> conferences = findConferencesForTimePeriod.execute(FindConferencesForTimePeriod.Request.builder()
                .from(request.getConferenceStartTimeFrom())
                .until(request.getConferenceStartTimeUntil())
                .build());
        Set<UUID> conferenceIds = conferences.stream().map(Conference::getId).collect(Collectors.toUnmodifiableSet());
        Map<UUID, Long> conferenceRegistrationCounts = findRegistrationsAmountsByConferenceIds.execute(FindRegistrationsAmountsByConferenceIds.Request.of(conferenceIds));
        Set<UUID> roomIds = conferences.stream().map(Conference::getConferenceRoomId).collect(Collectors.toUnmodifiableSet());
        Map<UUID, ConferenceRoom> conferenceRooms = findConferenceRooms.execute(FindConferenceRooms.Request.of(roomIds));

        return conferences.stream()
                .map(conference -> ConferenceDetails.builder()
                        .conferenceId(conference.getId())
                        .conferenceStartTime(conference.getStartTime())
                        .conferenceEndTime(conference.getEndTime())
                        .conferenceLocation(conferenceRooms.get(conference.getConferenceRoomId()).getLocation())
                        .conferenceCapacity(conferenceRooms.get(conference.getConferenceRoomId()).getCapacity())
                        .numberOfRegistrations(conferenceRegistrationCounts.getOrDefault(conference.getId(), 0L))
                        .build())
                .collect(Collectors.toUnmodifiableSet());
    }

    @Value
    @Builder
    public static class ConferenceDetails {
        UUID conferenceId;
        LocalDateTime conferenceStartTime;
        LocalDateTime conferenceEndTime;
        String conferenceLocation;
        Long conferenceCapacity;
        Long numberOfRegistrations;
    }

    @Value
    @Builder
    public static class Request {
        LocalDateTime conferenceStartTimeFrom;
        LocalDateTime conferenceStartTimeUntil;
    }
}
