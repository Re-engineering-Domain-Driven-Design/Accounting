package reengineering.ddd.accounting;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class FlywayConfig {
    @Bean
    public static FlywayMigrationStrategy cleanMigrateStrategy() {

        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }
}
