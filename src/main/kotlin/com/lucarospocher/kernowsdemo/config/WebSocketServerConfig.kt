package com.lucarospocher.kernowsdemo.config

import com.lucarospocher.kernowsdemo.handler.ChatWebSocketHandler
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import reactor.core.publisher.Hooks
import java.util.Map

@Configuration
class WebSocketServerConfig {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun webSocketMapping(webSocketHandler: ChatWebSocketHandler): HandlerMapping {
        return SimpleUrlHandlerMapping(Map.of<String, Any?>("/chat", webSocketHandler), -1)
    }

    @Bean
    fun errorDroppedHandler() = Hooks.onErrorDropped { error: Throwable ->
        log.warn("Exception happened: ", error)
    }
}