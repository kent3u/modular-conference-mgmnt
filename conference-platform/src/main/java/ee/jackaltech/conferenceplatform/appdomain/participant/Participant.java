package ee.jackaltech.conferenceplatform.appdomain.participant;

import ee.jackaltech.conferenceplatform.adapter.database.conference.ParticipantEntity.Gender;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
public class Participant {

    UUID id;
    String firstName;
    String lastName;
    String email;
    Gender gender;
    LocalDate dateOfBirth;
}
