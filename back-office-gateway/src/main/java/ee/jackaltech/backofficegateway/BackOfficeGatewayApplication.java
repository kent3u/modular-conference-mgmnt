package ee.jackaltech.backofficegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ee.jackaltech.backofficegateway", "ee.jackaltech.conferenceplatform"})
public class BackOfficeGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackOfficeGatewayApplication.class, args);
    }

}
