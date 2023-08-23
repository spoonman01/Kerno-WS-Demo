package com.lucarospocher.kernowsdemo.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service

/**
 * Kafka producer used by all WS sessions each time a message is received from the client.
 */
@Service
class KafkaProducerService(
    private val reactiveKafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, String>
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Value(value = "\${KAFKA_TOPIC}")
    private lateinit var topic: String

    fun send(message: String) {
        log.info("Sending to topic=$topic, message=$message")
        reactiveKafkaProducerTemplate.send(topic, message)
            .doOnSuccess { senderResult ->
                log.debug("Sent topic=$topic, message=$message, offset=${senderResult.recordMetadata().offset()}")
            }
            .subscribe()
    }
}