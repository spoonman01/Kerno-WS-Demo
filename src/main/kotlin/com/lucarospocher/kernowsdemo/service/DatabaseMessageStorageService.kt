package com.lucarospocher.kernowsdemo.service

import com.lucarospocher.kernowsdemo.model.Message
import com.lucarospocher.kernowsdemo.repository.MessageWriteRepository
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

/**
 * Listens to Kafka queue (via shared Flux<String>) and inserts all messages in database table.
 */
@Service
class DatabaseMessageStorageService(
    @Qualifier("kafkaBroadcastFlux") private val kafkaInboundFlux: Flux<String>,
    private val messageWriteRepository: MessageWriteRepository,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    init {
        log.info("Initialising chat storage hook...")
        kafkaInboundFlux.subscribe { runBlocking { messageWriteRepository.save(Message(text = it)) } }
    }
}