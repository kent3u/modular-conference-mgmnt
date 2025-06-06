package ee.jackaltech.conferencegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ee.jackaltech.conferencegateway", "ee.jackaltech.conferenceplatform"})
public class ConferenceGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConferenceGatewayApplication.class, args);
    }
}
