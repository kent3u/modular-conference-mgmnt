package ee.jackaltech.conferenceplatform;

import ee.jackaltech.conferenceplatform.fixedtime.TestClock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;

/**
 * Used to create Beans that are defined in test package
 */
@Configuration
public class TestBeanConfig {
    @Bean
    @Primary
    Clock primaryClock() {
        return TestClock.newInstance();
    }
}
