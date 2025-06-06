package ee.jackaltech.conferencegateway.adapter.web.conference;

import ee.jackaltech.conferenceplatform.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ConferenceControllerTest extends BaseIntegrationTest {

    @Test
    @Sql("/testdata/conference/sql/find_available_conferences.sql")
    void findAvailableConferences_validData_returnAvailableConferences() throws Exception {
        mockMvc.perform(get("/api/conference")
                        .queryParam("startTimeFrom", "2025-01-01T15:30")
                        .queryParam("startTimeUntil", "2025-12-12T15:30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[?(@.conferenceId == 'aa74b20a-e9a1-4a0d-8e1b-0a3df3e8ff18' " +
                        "&& @.conferenceStartTime == '2025-11-11T14:30:00' " +
                        "&& @.conferenceEndTime == '2025-11-11T16:30:00' " +
                        "&& @.conferenceLocation == 'Viru 1, 3rd floor, room 123' " +
                        "&& @.conferenceCapacity == 25 " +
                        "&& @.numberOfRegistrations == 2)]").exists())
                .andExpect(jsonPath("$.[?(@.conferenceId == 'e10bf005-577d-462d-bc23-067557f7e016' " +
                        "&& @.conferenceStartTime == '2025-12-11T14:30:00' " +
                        "&& @.conferenceEndTime == '2025-12-11T16:30:00' " +
                        "&& @.conferenceLocation == 'Viru 1, 3rd floor, room 123' " +
                        "&& @.conferenceCapacity == 25 " +
                        "&& @.numberOfRegistrations == 0)]").exists());
    }
}
