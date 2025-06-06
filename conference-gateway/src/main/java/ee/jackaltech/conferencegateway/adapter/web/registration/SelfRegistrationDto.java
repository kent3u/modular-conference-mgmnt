package ee.jackaltech.conferencegateway.adapter.web.registration;

import ee.jackaltech.conferenceplatform.adapter.database.conference.ParticipantEntity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class SelfRegistrationDto {

    private UUID conferenceId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private LocalDate dateOfBirth;
}
