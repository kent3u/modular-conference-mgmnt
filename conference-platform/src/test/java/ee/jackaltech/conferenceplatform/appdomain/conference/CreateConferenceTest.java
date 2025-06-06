package ee.jackaltech.conferenceplatform.appdomain.conference;

import ee.jackaltech.conferenceplatform.adapter.database.conference.ConferenceRoomEntity;
import ee.jackaltech.conferenceplatform.appdomain.conference.exception.ConferenceOverlapException;
import ee.jackaltech.conferenceplatform.appdomain.conference.exception.ConferenceTimeException;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.ConferenceRoom;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.GetConferenceRoom;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception.ConferenceRoomUnderConstructionException;
import ee.jackaltech.conferenceplatform.appdomain.conferenceroom.exception.NoSuchConferenceRoomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateConferenceTest {

    private static final UUID SAMPLE_ROOM_ID = UUID.fromString("c12a0417-a73f-4528-92af-8cf1d7bff17f");
    private static final LocalDateTime SAMPLE_DATE_TIME = LocalDateTime.parse("2025-01-01T21:00");

    @Mock
    private SaveConference saveConference;
    @Mock
    private GetConferenceRoom getConferenceRoom;
    @Mock
    private ExistsConferenceOverlapInRoom existsConferenceOverlapInRoom;

    private final Clock clock = Clock.fixed(Instant.parse("2023-01-15T10:00:00.00Z"), ZoneId.systemDefault());

    private CreateConference createConference;

    @Captor
    private ArgumentCaptor<SaveConference.Request> conferenceRequestCaptor;

    @BeforeEach
    void setUp() {
        createConference = new CreateConference(saveConference, getConferenceRoom, existsConferenceOverlapInRoom, clock);
    }

    @Test
    void execute_validData_saved() {
        when(getConferenceRoom.execute(GetConferenceRoom.Request.of(SAMPLE_ROOM_ID)))
                .thenReturn(Optional.ofNullable(ConferenceRoom.builder()
                        .id(SAMPLE_ROOM_ID)
                        .name("SAMPLER")
                        .location("Sample location 12a")
                        .status(ConferenceRoomEntity.ConferenceRoomStatus.AVAILABLE)
                        .capacity(25L)
                        .build()));
        mockConferenceOverlapInRoom(false);

        createConference.execute(composeValidRequest());

        verify(saveConference).execute(conferenceRequestCaptor.capture());
        Conference conference = conferenceRequestCaptor.getValue().getConference();
        assertThat(conference.getId()).isNotNull();
        assertThat(conference.getConferenceRoomId()).isEqualTo(SAMPLE_ROOM_ID);
        assertThat(conference.getStartTime()).isEqualTo(SAMPLE_DATE_TIME);
        assertThat(conference.getEndTime()).isEqualTo(SAMPLE_DATE_TIME.plusHours(2));
    }

    @Test
    void execute_startTimeAfterEndTime_throwsConferenceTimeException() {
        CreateConference.Request request = CreateConference.Request.builder()
                .conferenceRoomId(SAMPLE_ROOM_ID)
                .conferenceStartTime(SAMPLE_DATE_TIME.plusHours(2))
                .conferenceEndTime(SAMPLE_DATE_TIME)
                .build();

        assertThrows(ConferenceTimeException.class, () -> createConference.execute(request));
    }

    @Test
    void execute_noSuchConferenceRoom_throwsNoSuchConferenceRoomException() {
        CreateConference.Request request = composeValidRequest();

        assertThrows(NoSuchConferenceRoomException.class, () -> createConference.execute(request));
    }

    @Test
    void execute_roomUnderConstruction_throwsConferenceRoomUnderConstructionException() {
        when(getConferenceRoom.execute(GetConferenceRoom.Request.of(SAMPLE_ROOM_ID)))
                .thenReturn(Optional.ofNullable(ConferenceRoom.builder()
                        .id(SAMPLE_ROOM_ID)
                        .name("SAMPLER")
                        .location("Sample location 12a")
                        .status(ConferenceRoomEntity.ConferenceRoomStatus.UNDER_CONSTRUCTION)
                        .capacity(25L)
                        .build()));
        CreateConference.Request request = composeValidRequest();

        assertThrows(ConferenceRoomUnderConstructionException.class, () -> createConference.execute(request));
    }

    @Test
    void execute_conferencesAtTheSameTime_throwsConferenceOverlapException() {
        when(getConferenceRoom.execute(GetConferenceRoom.Request.of(SAMPLE_ROOM_ID)))
                .thenReturn(Optional.ofNullable(ConferenceRoom.builder()
                        .id(SAMPLE_ROOM_ID)
                        .name("SAMPLER")
                        .location("Sample location 12a")
                        .status(ConferenceRoomEntity.ConferenceRoomStatus.AVAILABLE)
                        .capacity(25L)
                        .build()));
        mockConferenceOverlapInRoom(true);

        CreateConference.Request request = composeValidRequest();

        assertThrows(ConferenceOverlapException.class, () -> createConference.execute(request));
    }

    private void mockConferenceOverlapInRoom(boolean t) {
        when(existsConferenceOverlapInRoom.execute(ExistsConferenceOverlapInRoom.Request.builder()
                .conferenceRoomId(SAMPLE_ROOM_ID)
                .conferenceStartTime(SAMPLE_DATE_TIME)
                .conferenceEndTime(SAMPLE_DATE_TIME.plusHours(2))
                .build())).thenReturn(t);
    }

    private CreateConference.Request composeValidRequest() {
        return CreateConference.Request.builder()
                .conferenceRoomId(SAMPLE_ROOM_ID)
                .conferenceStartTime(SAMPLE_DATE_TIME)
                .conferenceEndTime(SAMPLE_DATE_TIME.plusHours(2))
                .build();
    }
}
