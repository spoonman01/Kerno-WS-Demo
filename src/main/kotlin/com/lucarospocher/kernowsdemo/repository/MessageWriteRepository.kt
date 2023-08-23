package com.lucarospocher.kernowsdemo.repository

import com.lucarospocher.kernowsdemo.model.Message

interface MessageWriteRepository {
    fun save(message: Message): Message
}