package ee.jackaltech.conferencegateway.adapter.web.registration;

import ee.jackaltech.conferenceplatform.appdomain.registration.CancelRegistration;
import ee.jackaltech.conferenceplatform.appdomain.registration.Registration.ConferenceParticipantCode;
import ee.jackaltech.conferenceplatform.appdomain.registration.SelfRegistration;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registration")
class RegistrationController {

    private final SelfRegistration selfRegistration;
    private final CancelRegistration cancelRegistration;

    @PostMapping
    public SelfRegistrationCodeDto selfRegister(@RequestBody SelfRegistrationDto selfRegistrationDto) {
        var selfRegistrationCode = selfRegistration.execute(SelfRegistration.Request.builder()
                .conferenceId(selfRegistrationDto.getConferenceId())
                .firstName(selfRegistrationDto.getFirstName())
                .lastName(selfRegistrationDto.getLastName())
                .email(selfRegistrationDto.getEmail())
                .gender(selfRegistrationDto.getGender())
                .dateOfBirth(selfRegistrationDto.getDateOfBirth())
                .build());
        return SelfRegistrationCodeDto.of(selfRegistrationCode.getCode());
    }

    @DeleteMapping("/{conferenceParticipantCode}")
    public void cancelRegistration(@PathVariable String conferenceParticipantCode) {
        cancelRegistration.execute(CancelRegistration.Request.of(ConferenceParticipantCode.of(conferenceParticipantCode)));
    }
}
