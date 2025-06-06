package ee.jackaltech.conferenceplatform.adapter.database.conference;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "participant")
public class ParticipantEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    @Column(name = "email")
    private String email;
    @Column(name = "gender")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    public enum Gender {
        FEMALE, MALE
    }
}
