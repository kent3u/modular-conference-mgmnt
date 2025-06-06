package ee.jackaltech.backofficegateway.adapter.web.conference;

import com.fasterxml.jackson.core.JsonProcessingException;
import ee.jackaltech.conferenceplatform.BaseIntegrationTest;
import ee.jackaltech.conferenceplatform.appdomain.conference.FindConferencesForTimePeriod;
import ee.jackaltech.conferenceplatform.appdomain.conference.GetConference;
import ee.jackaltech.conferenceplatform.appdomain.registration.FindConferenceRegistrationAmount;
import ee.jackaltech.conferenceplatform.fixedtime.FixedTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ConferenceControllerTest extends BaseIntegrationTest {

    private static final UUID SAMPLE_CONFERENCE_ROOM_ID = UUID.fromString("2d95af9f-88dc-408e-9bc1-658ef1c53683");
    private static final LocalDateTime SAMPLE_DATETIME = LocalDateTime.parse("2025-11-11T10:30");
    private static final UUID SAMPLE_CONFERENCE_ID = UUID.fromString("aa74b20a-e9a1-4a0d-8e1b-0a3df3e8ff18");

    @Autowired
    private Clock clock;
    @Autowired
    private FindConferencesForTimePeriod findConferencesForTimePeriod;
    @Autowired
    private GetConference getConference;
    @Autowired
    private FindConferenceRegistrationAmount findConferenceRegistrationAmount;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @FixedTime("2025-10-10T20:30")
    @Sql("/testdata/conference/sql/conference_creation_data.sql")
    void createConference_validData_createsConference() throws Exception {
        mockMvc.perform(post("/api/conference")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(composeConferenceCreationJson()))
                .andExpect(status().isOk());

        assertThat(findConferencesForTimePeriod.execute(FindConferencesForTimePeriod.Request.builder()
                .from(SAMPLE_DATETIME)
                .until(SAMPLE_DATETIME)
                .build())).singleElement()
                .satisfies(conference -> {
                            assertThat(conference.getConferenceRoomId()).isEqualTo(SAMPLE_CONFERENCE_ROOM_ID);
                            assertThat(conference.getStartTime()).isEqualTo(SAMPLE_DATETIME);
                            assertThat(conference.getEndTime()).isEqualTo(SAMPLE_DATETIME.plusDays(1));
                        }
                );
    }

    @Test
    void createConference_startTimeAfterEndTime_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/conference")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ConferenceCreationDto.builder()
                                .conferenceRoomId(SAMPLE_CONFERENCE_ROOM_ID)
                                .conferenceStartTime(SAMPLE_DATETIME)
                                .conferenceEndTime(SAMPLE_DATETIME.minusDays(1))
                                .build())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createConference_noSuchConferenceRoom_returnsUnprocessableEntity() throws Exception {
        mockMvc.perform(post("/api/conference")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(composeConferenceCreationJson()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Sql("/testdata/conference/sql/conference_creation_data_room_under_construction.sql")
    void createConference_conferenceRoomUnderConstruction_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/conference")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ConferenceCreationDto.builder()
                                .conferenceRoomId(UUID.fromString("490a4c38-91c7-4264-9161-a4dc544d58e8"))
                                .conferenceStartTime(SAMPLE_DATETIME)
                                .conferenceEndTime(SAMPLE_DATETIME.minusDays(1))
                                .build())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @FixedTime("2025-10-10T20:30")
    @Sql("/testdata/conference/sql/conference_creation_conference_overlap.sql")
    void createConference_conferenceOverlapWithExistingConference_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/conference")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(composeConferenceCreationJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @FixedTime("2025-10-10T20:30")
    @Sql("/testdata/conference/sql/conference_change_data.sql")
    void changeConference_validData_conferenceChanged() throws Exception {
        mockMvc.perform(put(format("/api/conference/%s", SAMPLE_CONFERENCE_ID))
                        .content(composeConferenceChangeDto())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(findConferencesForTimePeriod.execute(FindConferencesForTimePeriod.Request.builder()
                .from(SAMPLE_DATETIME)
                .until(SAMPLE_DATETIME.plusDays(3))
                .build()))
                .singleElement()
                .satisfies(conference -> {
                    assertThat(conference.getId()).isEqualTo(SAMPLE_CONFERENCE_ID);
                    assertThat(conference.getConferenceRoomId()).isEqualTo(SAMPLE_CONFERENCE_ROOM_ID);
                    assertThat(conference.getStartTime()).isEqualTo(SAMPLE_DATETIME.plusDays(2));
                    assertThat(conference.getEndTime()).isEqualTo(SAMPLE_DATETIME.plusDays(3));
                });
    }

    @Test
    void changeConference_noConferenceExists_returnsUnprocessableEntity() throws Exception {
        mockMvc.perform(put(format("/api/conference/%s", SAMPLE_CONFERENCE_ID))
                        .content(composeConferenceChangeDto())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @FixedTime("2025-10-10T20:30")
    @Sql("/testdata/conference/sql/conference_change_data.sql")
    void changeConference_newStartTimeInThePast_returnsBadRequest() throws Exception {
        mockMvc.perform(put(format("/api/conference/%s", SAMPLE_CONFERENCE_ID))
                        .content(objectMapper.writeValueAsString(
                                ConferenceChangeDto.builder()
                                        .conferenceRoomId(SAMPLE_CONFERENCE_ROOM_ID)
                                        .conferenceStartTime(LocalDateTime.parse("2024-10-10T10:10"))
                                        .conferenceEndTime(LocalDateTime.parse("2024-10-11T10:10"))
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @FixedTime("2025-10-10T20:30")
    @Sql("/testdata/conference/sql/conference_change_data.sql")
    void changeConference_roomDoesntExist_returnsUnprocessableEntity() throws Exception {
        mockMvc.perform(put(format("/api/conference/%s", SAMPLE_CONFERENCE_ID))
                        .content(objectMapper.writeValueAsString(
                                ConferenceChangeDto.builder()
                                        .conferenceRoomId(UUID.fromString("7a453fd2-4d20-45ec-82b4-d736e5c5f28c"))
                                        .conferenceStartTime(SAMPLE_DATETIME)
                                        .conferenceEndTime(SAMPLE_DATETIME)
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @FixedTime("2025-10-10T20:30")
    @Sql("/testdata/conference/sql/conference_change_conference_overlap.sql")
    void changeConference_existsConferenceOverlapInRoom_returnsBadRequest() throws Exception {
        mockMvc.perform(put(format("/api/conference/%s", SAMPLE_CONFERENCE_ID))
                        .content(composeConferenceChangeDto())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @FixedTime("2025-10-10T20:30")
    @Sql("/testdata/conference/sql/conference_change_with_registrations.sql")
    void changeConference_registrationAmountExceededInNewRoom_returnsBadRequest() throws Exception {
        mockMvc.perform(put(format("/api/conference/%s", SAMPLE_CONFERENCE_ID))
                        .content(objectMapper.writeValueAsString(
                                ConferenceChangeDto.builder()
                                        .conferenceRoomId(UUID.fromString("28182ccc-b81b-459a-be16-bac00333d246"))
                                        .conferenceStartTime(SAMPLE_DATETIME)
                                        .conferenceEndTime(SAMPLE_DATETIME.plusDays(1))
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @FixedTime("2025-10-10T20:30")
    @Sql("/testdata/conference/sql/conference_change_room_under_construction.sql")
    void changeConference_newRoomUnderConstruction_returnsBadRequest() throws Exception {
        mockMvc.perform(put(format("/api/conference/%s", SAMPLE_CONFERENCE_ID))
                        .content(objectMapper.writeValueAsString(
                                ConferenceChangeDto.builder()
                                        .conferenceRoomId(UUID.fromString("28182ccc-b81b-459a-be16-bac00333d246"))
                                        .conferenceStartTime(SAMPLE_DATETIME)
                                        .conferenceEndTime(SAMPLE_DATETIME.plusDays(1))
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @FixedTime("2025-10-10T20:30")
    @Sql("/testdata/conference/sql/conference_cancel_registrations.sql")
    void cancelConference_validData_conferenceDeletedWithRegistration() throws Exception {
        mockMvc.perform(delete(format("/api/conference/%s", SAMPLE_CONFERENCE_ID)))
                .andExpect(status().isOk());

        testEntityManager.clear(); // fetches cached conference previously queried by method in the same transaction
        assertThat(getConference.execute(GetConference.Request.of(SAMPLE_CONFERENCE_ID))).isEmpty();
        assertThat(findConferenceRegistrationAmount.execute(FindConferenceRegistrationAmount.Request.of(SAMPLE_CONFERENCE_ID)))
                .isEqualTo(0L);
    }

    @Test
    void cancelConference_noSuchConference_returnsUnprocessableEntity() throws Exception {
        mockMvc.perform(delete(format("/api/conference/%s", SAMPLE_CONFERENCE_ID)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @FixedTime("2025-11-11T15:30")
    @Sql("/testdata/conference/sql/conference_cancel_registrations.sql")
    void cancelConference_conferenceIsAlreadyTakingPlace_returnsBadRequest() throws Exception {
        mockMvc.perform(delete(format("/api/conference/%s", SAMPLE_CONFERENCE_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql("/testdata/conference/sql/conference_availability_registrations.sql")
    void getConferenceAvailability_validData_returnsConferenceAvailability() throws Exception {
        mockMvc.perform(get(format("/api/conference/%s/availability", SAMPLE_CONFERENCE_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conferenceId").value(SAMPLE_CONFERENCE_ID.toString()))
                .andExpect(jsonPath("$.conferenceRoomCapacity").value(25L))
                .andExpect(jsonPath("$.numberOfParticipants").value(2L));
    }

    @Test
    void getConferenceAvailability_noSuchConference_returnsUnprocessableEntity() throws Exception {
        mockMvc.perform(get(format("/api/conference/%s/availability", SAMPLE_CONFERENCE_ID)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Sql("/testdata/conference/sql/conference_feedback.sql")
    void getConferenceFeedback_validData_returnsFeedback() throws Exception {
        mockMvc.perform(get(format("/api/conference/%s/feedback", SAMPLE_CONFERENCE_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conferenceId").value(SAMPLE_CONFERENCE_ID.toString()))
                .andExpect(jsonPath("$.participantFeedbacks.length()").value(1))
                .andExpect(jsonPath("$.participantFeedbacks[0].participantName").value("Jane D**"))
                .andExpect(jsonPath("$.participantFeedbacks[0].feedback").value("Cool event."));
    }

    @Test
    void getConferenceFeedback_noSuchConference_returnsUnprocessableEntity() throws Exception {
        mockMvc.perform(get(format("/api/conference/%s/feedback", SAMPLE_CONFERENCE_ID)))
                .andExpect(status().isUnprocessableEntity());
    }

    private String composeConferenceChangeDto() throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ConferenceChangeDto.builder()
                        .conferenceRoomId(SAMPLE_CONFERENCE_ROOM_ID)
                        .conferenceStartTime(SAMPLE_DATETIME.plusDays(2))
                        .conferenceEndTime(SAMPLE_DATETIME.plusDays(3))
                        .build()
        );
    }


    private String composeConferenceCreationJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ConferenceCreationDto.builder()
                        .conferenceRoomId(SAMPLE_CONFERENCE_ROOM_ID)
                        .conferenceStartTime(SAMPLE_DATETIME)
                        .conferenceEndTime(SAMPLE_DATETIME.plusDays(1))
                        .build()
        );
    }
}
