package com.movies

import io.r2dbc.spi.ConnectionFactory
import mu.KLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.FileSystemResource
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import kotlin.math.log

@SpringBootApplication
class MoviesRestfulApiApplication

fun main(args: Array<String>) {
	runApplication<MoviesRestfulApiApplication>(*args)
}

