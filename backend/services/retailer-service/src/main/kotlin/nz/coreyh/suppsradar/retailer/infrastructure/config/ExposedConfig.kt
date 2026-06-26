package nz.coreyh.suppsradar.retailer.infrastructure.config

import org.jetbrains.exposed.v1.spring.boot4.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration
import org.springframework.context.annotation.Configuration

/**
 * Configures Exposed's Spring Boot auto‑configuration.
 *
 * We explicitly exclude [DataSourceTransactionManagerAutoConfiguration] because
 * Exposed manages its own transaction lifecycle and does not use Spring’s
 * JDBC-based transaction manager.
 */
@Configuration
@ImportAutoConfiguration(
    value = [ExposedAutoConfiguration::class],
    exclude = [DataSourceTransactionManagerAutoConfiguration::class],
)
class ExposedConfig
