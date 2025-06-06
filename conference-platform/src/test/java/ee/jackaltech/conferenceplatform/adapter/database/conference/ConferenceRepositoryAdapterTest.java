package ee.jackaltech.conferenceplatform.adapter.database.conference;

import ee.jackaltech.conferenceplatform.BaseIntegrationTest;
import ee.jackaltech.conferenceplatform.adapter.database.conference.ConferenceRoomEntity.ConferenceRoomStatus;
import ee.jackaltech.conferenceplatform.appdomain.conference.Conference;
import ee.jackaltech.conferenceplatform.appdomain.conference.DeleteConference;
import ee.jackaltech.conferenceplatform.appdomain.conference.ExistsConferenceInRoomInTheFuture;
import ee.jackaltech.conferenceplatform.appdomain.conference.ExistsConferenceOverlapInRoom;
import ee.jackaltech.conferenceplatform.appdomain.conference.ExistsFutureConferenceWithMinRegistrationsByRoomId;
import ee.jackaltech.conferenceplatform.appdomain.conference.FindConferencesForTimePeriod;
import ee.jackaltech.conferenceplatform.appdomain.conference.GetConference;
import ee.jackaltech.conferenceplatform.appdomain.conference.SaveConference;
import ee.jackaltech.conferenceplatform.fixedtime.FixedTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ConferenceRepositoryAdapterTest extends BaseIntegrationTest {

    private static final LocalDateTime SAMPLE_DATE_TIME = LocalDateTime.parse("2023-01-01T10:30");
    private static final UUID SAMPLE_UUID = UUID.fromString("c12a0417-a73f-4528-92af-8cf1d7bff17f");
    private static final UUID SAMPLE_ROOM_ID = UUID.fromString("4bb14e93-08c3-4657-9129-314ac20e7b5a");

    @Autowired
    private ConferenceEntityRepository conferenceEntityRepository;
    @Autowired
    private ConferenceRoomEntityRepository conferenceRoomEntityRepository;
    @Autowired
    private ParticipantEntityRepository participantEntityRepository;
    @Autowired
    private RegistrationEntityRepository registrationEntityRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ConferenceRepositoryAdapter adapter;

    @BeforeEach
    void clearState() {
        conferenceEntityRepository.deleteAll();
    }

    @Test
    @FixedTime("2025-01-01T10:30")
    void existsConferenceOverlapInRoom_twoConferencesOnDifferentTimes_noOverlap() {
        conferenceRoomEntityRepository.save(composeSampleConferenceRoomBuilder().build());
        conferenceEntityRepository.save(composeConferenceEntityBuilder().build());
        conferenceEntityRepository.save(composeConferenceEntityBuilder()
                .id(UUID.randomUUID())
                .startTime(SAMPLE_DATE_TIME.plusDays(1))
                .endTime(SAMPLE_DATE_TIME.plusDays(2))
                .build());

        boolean result = adapter.execute(ExistsConferenceOverlapInRoom.Request.builder()
                .conferenceRoomId(SAMPLE_ROOM_ID)
                .conferenceStartTime(SAMPLE_DATE_TIME.plusHours(2))
                .conferenceEndTime(SAMPLE_DATE_TIME.plusHours(3))
                .build());

        assertThat(result).isFalse();
    }

    @Test
    @FixedTime("2023-01-01T10:00")
    void existsConferenceOverlapInRoom_conferenceOverlapsWithParameters_overlapExists() {
        conferenceRoomEntityRepository.save(composeSampleConferenceRoomBuilder().build());
        conferenceEntityRepository.save(composeConferenceEntityBuilder().build());

        boolean result = adapter.execute(ExistsConferenceOverlapInRoom.Request.builder()
                .conferenceRoomId(SAMPLE_ROOM_ID)
                .conferenceStartTime(SAMPLE_DATE_TIME.plusHours(1))
                .conferenceEndTime(SAMPLE_DATE_TIME.plusHours(3))
                .build());

        assertThat(result).isTrue();
    }

    @Test
    void saveConference_validData_conferenceSaved() {
        conferenceRoomEntityRepository.save(composeSampleConferenceRoomBuilder().build());
        adapter.execute(SaveConference.Request.of(Conference.builder()
                .id(SAMPLE_UUID)
                .conferenceRoomId(SAMPLE_ROOM_ID)
                .startTime(SAMPLE_DATE_TIME)
                .endTime(SAMPLE_DATE_TIME.plusHours(1))
                .build()));

        ConferenceEntity saved = conferenceEntityRepository.getReferenceById(SAMPLE_UUID);

        assertThat(saved.getId()).isEqualTo(SAMPLE_UUID);
        assertThat(saved.getConferenceRoomId()).isEqualTo(SAMPLE_ROOM_ID);
        assertThat(saved.getStartTime()).isEqualTo(SAMPLE_DATE_TIME);
        assertThat(saved.getEndTime()).isEqualTo(SAMPLE_DATE_TIME.plusHours(1));
        assertThat(saved.getDeletedAt()).isNull();
    }

    @Test
    void getConference_dataExists_conferenceReturned() {
        conferenceRoomEntityRepository.save(composeSampleConferenceRoomBuilder().build());
        conferenceEntityRepository.save(composeConferenceEntityBuilder().build());

        Optional<Conference> result = adapter.execute(GetConference.Request.of(SAMPLE_UUID));

        assertThat(result).hasValueSatisfying(conference -> {
            assertThat(conference.getId()).isEqualTo(SAMPLE_UUID);
            assertThat(conference.getConferenceRoomId()).isEqualTo(SAMPLE_ROOM_ID);
            assertThat(conference.getStartTime()).isEqualTo(SAMPLE_DATE_TIME);
            assertThat(conference.getEndTime()).isEqualTo(SAMPLE_DATE_TIME.plusHours(2));
        });
    }

    @Test
    void deleteConference_validData_conferenceNotFound() {
        conferenceRoomEntityRepository.save(composeSampleConferenceRoomBuilder().build());
        conferenceEntityRepository.save(composeConferenceEntityBuilder().build());

        adapter.execute(DeleteConference.Request.of(Conference.builder()
                .id(SAMPLE_UUID)
                .conferenceRoomId(SAMPLE_ROOM_ID)
                .startTime(SAMPLE_DATE_TIME)
                .endTime(SAMPLE_DATE_TIME.plusHours(2))
                .build()));

        testEntityManager.clear();
        assertThat(conferenceEntityRepository.findById(SAMPLE_UUID)).isEmpty();
    }

    @Test
    @FixedTime("2023-01-01T10:00")
    void existsConferenceInRoomInTheFuture_validData_returnsTrue() {
        conferenceRoomEntityRepository.save(composeSampleConferenceRoomBuilder().build());
        conferenceEntityRepository.save(composeConferenceEntityBuilder().build());

        boolean result = adapter.execute(ExistsConferenceInRoomInTheFuture.Request.of(SAMPLE_ROOM_ID));

        assertThat(result).isTrue();
    }

    @Test
    void existsConferenceInRoomInTheFuture_onlyOldData_returnsFalse() {
        conferenceRoomEntityRepository.save(composeSampleConferenceRoomBuilder().build());
        conferenceEntityRepository.save(composeConferenceEntityBuilder()
                .startTime(SAMPLE_DATE_TIME.minusYears(5).minusHours(2))
                .endTime(SAMPLE_DATE_TIME.minusYears(5))
                .build());

        boolean result = adapter.execute(ExistsConferenceInRoomInTheFuture.Request.of(SAMPLE_ROOM_ID));

        assertThat(result).isFalse();
    }

    @Test
    @FixedTime("2023-01-01T10:00")
    void existsFutureConferenceWithMinRegistrationsByRoomId_futureConferenceWithOneParticipant_returnsTrue() {
        conferenceRoomEntityRepository.save(composeSampleConferenceRoomBuilder().build());
        conferenceEntityRepository.save(composeConferenceEntityBuilder().build());
        UUID participantId = UUID.fromString("e8c0977f-9294-4095-a855-64eed1aa4d62");
        participantEntityRepository.save(ParticipantEntity.builder()
                .id(participantId)
                .email("test.test@test.ee")
                .firstName("Test")
                .lastName("Tester")
                .dateOfBirth(LocalDate.now())
                .gender(ParticipantEntity.Gender.MALE)
                .build());
        registrationEntityRepository.save(RegistrationEntity.builder()
                .id(UUID.randomUUID())
                .conferenceId(SAMPLE_UUID)
                .conferenceParticipantCode("123456")
                .participantId(participantId)
                .build());

        boolean result = adapter.execute(ExistsFutureConferenceWithMinRegistrationsByRoomId.Request.of(SAMPLE_ROOM_ID, 1L));

        assertThat(result).isFalse();
    }

    @Test
    void existsFutureConferenceWithMinRegistrationsByRoomId_noRegistrations_returnsFalse() {
        conferenceRoomEntityRepository.save(composeSampleConferenceRoomBuilder().build());
        conferenceEntityRepository.save(composeConferenceEntityBuilder().build());

        boolean result = adapter.execute(ExistsFutureConferenceWithMinRegistrationsByRoomId.Request.of(SAMPLE_ROOM_ID, 1L));

        assertThat(result).isFalse();
    }

    @Test
    void findConferencesForTimePeriod_conferencesExist_returnsOneConference() {
        conferenceRoomEntityRepository.save(composeSampleConferenceRoomBuilder().build());
        conferenceEntityRepository.save(composeConferenceEntityBuilder().build());
        conferenceEntityRepository.save(composeConferenceEntityBuilder()
                .id(UUID.randomUUID())
                .startTime(SAMPLE_DATE_TIME.plusHours(4))
                .endTime(SAMPLE_DATE_TIME.plusHours(5))
                .build());

        Set<Conference> result = adapter.execute(FindConferencesForTimePeriod.Request.builder()
                .from(SAMPLE_DATE_TIME.minusHours(1))
                .until(SAMPLE_DATE_TIME)
                .build());

        assertThat(result).singleElement().satisfies(conference -> {
            assertThat(conference.getId()).isEqualTo(SAMPLE_UUID);
            assertThat(conference.getConferenceRoomId()).isEqualTo(SAMPLE_ROOM_ID);
            assertThat(conference.getStartTime()).isEqualTo(SAMPLE_DATE_TIME);
            assertThat(conference.getEndTime()).isEqualTo(SAMPLE_DATE_TIME.plusHours(2));
        });
    }


    private ConferenceRoomEntity.ConferenceRoomEntityBuilder composeSampleConferenceRoomBuilder() {
        return ConferenceRoomEntity.builder()
                .id(SAMPLE_ROOM_ID)
                .name("Room")
                .status(ConferenceRoomStatus.AVAILABLE)
                .location("Location 12a")
                .capacity(25L);
    }

    private ConferenceEntity.ConferenceEntityBuilder composeConferenceEntityBuilder() {
        return ConferenceEntity.builder()
                .id(SAMPLE_UUID)
                .conferenceRoomId(SAMPLE_ROOM_ID)
                .startTime(SAMPLE_DATE_TIME)
                .endTime(SAMPLE_DATE_TIME.plusHours(2));
    }
}
