package ee.jackaltech.conferenceplatform;

import lombok.Getter;
import org.testcontainers.containers.PostgreSQLContainer;

public class TestDatabaseConfig {
    private static final String TESTCONTAINERS_DB_NAME = "postgres-testcontainers";

    @Getter
    private static final PostgreSQLContainer dbContainer;

    static {
        dbContainer = new PostgreSQLContainer("postgres:latest").withDatabaseName(TESTCONTAINERS_DB_NAME)
            .withUsername(TESTCONTAINERS_DB_NAME)
            .withPassword(TESTCONTAINERS_DB_NAME);
        dbContainer.start();
    }
}
