package com.lucarospocher.kernowsdemo.repository

import com.lucarospocher.kernowsdemo.model.Message
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux

interface MessageReadRepository : R2dbcRepository<Message, Long> {

    fun findBy(pageable: Pageable): Flux<Message>
}