package com.lucarospocher.kernowsdemo.repository

import com.lucarospocher.kernowsdemo.model.Message
import io.r2dbc.spi.Row
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.mapping.OutboundRow
import org.springframework.r2dbc.core.Parameter
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository
class MessageWriteRepositoryImpl(
    private val template: R2dbcEntityTemplate,
) : MessageWriteRepository {

    override fun save(message: Message): Message {
        return template.insert(message).block()!!
    }

    @ReadingConverter
    class MessageReadConverter : Converter<Row, Message> {
        override fun convert(row: Row): Message =
            Message(
                id = row.get("id", String::class.java)?.toLongOrNull(),
                text = row.get("text", String::class.java) ?: "",
                created = LocalDateTime.parse(row.get("created", String::class.java), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")),
            )
    }

    @WritingConverter
    class MessageWriteConverter : Converter<Message, OutboundRow> {
        override fun convert(message: Message): OutboundRow {
            return OutboundRow().apply {
                put("id", Parameter.fromOrEmpty(message.id, Long::class.java))
                put("text", Parameter.from(message.text))
                put("created", Parameter.fromOrEmpty(message.created, String::class.java))
            }
        }
    }
}