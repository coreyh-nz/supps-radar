package nz.coreyh.suppsradar.crawler

import org.jetbrains.exposed.v1.spring.boot4.autoconfigure.ExposedAutoConfiguration
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@EnableAutoConfiguration(
    exclude = [ExposedAutoConfiguration::class, DataSourceAutoConfiguration::class], // TODO - setup test containers
)
class CrawlerApplicationTests {
    @Test
    fun contextLoads() {
    }
}
