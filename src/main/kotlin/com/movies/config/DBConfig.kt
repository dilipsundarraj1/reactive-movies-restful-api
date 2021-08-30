package com.movies.config

import com.movies.MoviesRestfulApiApplication
import io.r2dbc.spi.ConnectionFactory
import mu.KLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
class DBConfig {

    companion object : KLogging()


    @Bean
    fun initializer(connectionFactory: ConnectionFactory) =
        ConnectionFactoryInitializer().apply {
            setConnectionFactory(connectionFactory)
            setDatabasePopulator(
                CompositeDatabasePopulator()
                    .apply {
                        logger.info("Inside the DB initializer")
                        addPopulators(ResourceDatabasePopulator(ClassPathResource("sql/schema.sql")))
                    })
        }
}