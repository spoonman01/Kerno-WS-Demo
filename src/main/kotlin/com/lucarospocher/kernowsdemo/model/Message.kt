package com.lucarospocher.kernowsdemo.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("message")
data class Message (
    @Id
    val id: Long? = null,
    val text: String,
    val created: LocalDateTime? = null,
)