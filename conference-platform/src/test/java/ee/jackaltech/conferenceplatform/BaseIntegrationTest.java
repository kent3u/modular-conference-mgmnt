package ee.jackaltech.conferenceplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.jackaltech.conferenceplatform.fixedtime.FixedTimeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

@Transactional
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Import(TestcontainersConfiguration.class)
@TestExecutionListeners(value = {
        FixedTimeTestExecutionListener.class,
}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        PostgreSQLContainer dbContainer = TestDatabaseConfig.getDbContainer();
        registry.add("spring.datasource.url", dbContainer::getJdbcUrl);
        registry.add("spring.datasource.username", dbContainer::getUsername);
        registry.add("spring.datasource.password", dbContainer::getPassword);
        registry.add("spring.datasource.driverClassName", dbContainer::getDriverClassName);
    }
}
