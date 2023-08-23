package com.lucarospocher.kernowsdemo.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.core.publisher.Flux
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.SenderOptions


@Configuration
class KafkaConfig {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun reactiveKafkaProducerTemplate(properties: KafkaProperties): ReactiveKafkaProducerTemplate<String, String> {
        val props = properties.buildProducerProperties()
        return ReactiveKafkaProducerTemplate<String, String>(SenderOptions.create(props))
    }

    @Bean
    fun kafkaReceiverOptions(
        @Value(value = "\${KAFKA_TOPIC}") topic: String,
        @Value(value = "\${KAFKA_GROUP_BROADCAST}") groupBroadcast: String,
        kafkaProperties: KafkaProperties
    ): ReceiverOptions<String, String> {
        kafkaProperties.consumer.groupId = groupBroadcast
        val basicReceiverOptions: ReceiverOptions<String, String> =
            ReceiverOptions.create(kafkaProperties.buildConsumerProperties())
        return basicReceiverOptions.subscription(listOf(topic))
    }

    @Bean
    fun reactiveKafkaConsumerTemplate(kafkaReceiverOptions: ReceiverOptions<String, String>): ReactiveKafkaConsumerTemplate<String, String> {
        return ReactiveKafkaConsumerTemplate(kafkaReceiverOptions)
    }

    /**
     * Created as @Bean in order to avoid each WS session being a new Kafka listener.
     */
    @Bean
    @Lazy(false)
    @Qualifier("kafkaBroadcastFlux")
    fun reactiveKafkaConsumerFlux(reactiveKafkaConsumerTemplate: ReactiveKafkaConsumerTemplate<String, String>): Flux<String> {
        return reactiveKafkaConsumerTemplate
            .receiveAutoAck()
            .doOnNext { log.info("Received $it") }
            .map { it.value().toString() }
            .doOnError { log.error("Exception while consuming Kafka queue", it) }
            .share()
    }
}