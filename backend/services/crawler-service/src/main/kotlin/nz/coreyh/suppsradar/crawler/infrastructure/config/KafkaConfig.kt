package nz.coreyh.suppsradar.crawler.infrastructure.config

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.kafka.autoconfigure.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer
import org.springframework.kafka.support.serializer.JacksonJsonSerializer
import tools.jackson.databind.json.JsonMapper

/**
 * Kafka configuration.
 *
 * Kafka deserialization/serialization cannot be configured
 * via application properties, as doing so instantiates
 * [JacksonJsonDeserializer]/[JacksonJsonSerializer] with a plain new
 * [JsonMapper] instance rather than using Spring's autoconfigured
 * [JsonMapper] bean, which has the Kotlin module automatically registered.
 * Without the Kotlin module, data classes cannot be deserialised
 * correctly.
 */
@Configuration
class KafkaConfig(
    private val kafkaProperties: KafkaProperties,
    private val jsonMapper: JsonMapper,
) {
    /**
     * Configures how Kafka messages are deserialized when consumed by this
     * service.
     */
    @Bean
    fun consumerFactory(): ConsumerFactory<String, Any> {
        val deserializer = JacksonJsonDeserializer<Any>(jsonMapper)
        return DefaultKafkaConsumerFactory(
            kafkaProperties.buildConsumerProperties(),
            StringDeserializer(),
            deserializer,
        )
    }

    /**
     * Configures how Kafka messages are serialized when produced by this
     * service.
     */
    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        val serializer = JacksonJsonSerializer<Any>(jsonMapper)
        return DefaultKafkaProducerFactory(
            kafkaProperties.buildProducerProperties(),
            StringSerializer(),
            serializer,
        )
    }

    /**
     * Provides a [KafkaTemplate] for sending messages to Kafka topics.
     */
    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, Any>): KafkaTemplate<String, Any> = KafkaTemplate(producerFactory)

    /**
     * Configures the listener container factory used by all @[KafkaListener]
     * annotated methods.
     *
     * An explicit factory is required here because we supply a custom
     * [ConsumerFactory] with Jackson deserialization.
     */
    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, Any>,
    ): ConcurrentKafkaListenerContainerFactory<String, Any> =
        ConcurrentKafkaListenerContainerFactory<String, Any>().apply {
            setConsumerFactory(consumerFactory)
        }
}
