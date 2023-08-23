package com.lucarospocher.kernowsdemo.handler

import com.lucarospocher.kernowsdemo.repository.MessageReadRepository
import com.lucarospocher.kernowsdemo.service.KafkaProducerService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ChatWebSocketHandler(
    private val kafkaProducerService: KafkaProducerService,
    @Qualifier("kafkaBroadcastFlux") private val kafkaInboundFlux: Flux<String>,
    @Value(value = "\${MESSAGE_REPLAY_NUMBER}") var messageReplayNumber: Int,
    private val messageReadRepository: MessageReadRepository,
): WebSocketHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Reactive WS handler, sends messages to Kafka on each receive and sends historic messages in the beginning,
     *  then subscribes to a Flux attached to Kafka queue receiving messages from other WS sessions
     */
    override fun handle(session: WebSocketSession): Mono<Void> {
        return session.receive()
            .map { obj: WebSocketMessage -> obj.payloadAsText }
            .doOnSubscribe {
                log.info("New session initiated $it")
            }
            .doOnNext {
                kafkaProducerService.send(it)
            }
            .zipWith(
                // Flux concat fetched all the DB data first, then once completed listens indefinitely on the
                //  second Flux receiving the Kafka messages. All of them are sent back in the session.
                session.send(
                    (
                        messageReadRepository.findBy(PageRequest.of(0,messageReplayNumber)).map { it.text }
                            .concatWith(kafkaInboundFlux.map { it })
                    ).map { session.textMessage(it!!) }
                )
            )
            .then()
    }
}