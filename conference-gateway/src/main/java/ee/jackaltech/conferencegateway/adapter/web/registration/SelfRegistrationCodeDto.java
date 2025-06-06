package ee.jackaltech.conferencegateway.adapter.web.registration;

import lombok.Value;

@Value(staticConstructor = "of")
class SelfRegistrationCodeDto {

    String selfRegistrationCode;
}
