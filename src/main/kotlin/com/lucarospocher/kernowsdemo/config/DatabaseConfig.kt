package com.lucarospocher.kernowsdemo.config

import com.lucarospocher.kernowsdemo.repository.MessageWriteRepositoryImpl
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration

@Configuration
class DatabaseConfig : AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
            .option(ConnectionFactoryOptions.DRIVER, "postgresql")
            .option(ConnectionFactoryOptions.PROTOCOL, "r2dbc")
            .option(ConnectionFactoryOptions.HOST, "postgres")
            .option(ConnectionFactoryOptions.PORT, 5432)
            .option(ConnectionFactoryOptions.DATABASE, "chat")
            .option(ConnectionFactoryOptions.USER, "chat_write")
            .option(ConnectionFactoryOptions.PASSWORD, "C7SQKr@g6SwtXNi")
            .build()
        )
    }

    override fun getCustomConverters(): MutableList<Any> {
        return mutableListOf(
            MessageWriteRepositoryImpl.MessageReadConverter(),
            MessageWriteRepositoryImpl.MessageWriteConverter(),
        )
    }
}