package com.movies.repository

import com.movies.domain.MovieInfo
import com.movies.util.getMovieInfoK
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.dialect.H2Dialect
import org.springframework.r2dbc.core.DatabaseClient
import reactor.test.StepVerifier

@SpringBootTest
@Disabled
class MovieInfo1RepositoryTest  {

    @Autowired
    private val databaseClient: DatabaseClient? = null

    @Autowired
    lateinit var  movieInfoRepository : MovieInfoRepository

    @Test
    internal fun insertMovieInfo() {

        val movieInfo = getMovieInfoK()
        val template = R2dbcEntityTemplate(databaseClient!!, H2Dialect.INSTANCE)
        template.insert(MovieInfo::class.java).using(movieInfo).block()
        val movieInfoFlux = movieInfoRepository.findAll()
        StepVerifier.create(movieInfoFlux)
            .expectNextCount(1)
            .verifyComplete();

    }
}