package com.movies.controller

import com.movies.domain.MovieInfo
import com.movies.service.MovieInfoService
import com.movies.service.ReviewService
import com.movies.util.getMovieInfoKWithCast
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import kotlin.math.absoluteValue


@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
@ActiveProfiles("test")
class MovieInfoControllerTestIT {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var reviewService: ReviewService


    @Autowired
    lateinit var movieInfoService: MovieInfoService

/*    @AfterEach
    internal fun tearDown() {
        movieInfoService.deleteAllMovieInfo()
            .block()
    }*/

    @BeforeEach
    internal fun setUp() {
        reviewService.deleteAllReviews().block()
        movieInfoService.deleteAllMovieInfo()
            .block()
    }

    @Test
    internal fun movieInfoById() {

        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo = movieInfoService.saveMovieInfo(movieInfo).block()

        webTestClient.get().uri("/v1/movie_infos/{id}", savedMovieInfo?.movieInfoId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.name").isEqualTo("The Dark Knight")
            .jsonPath("$.year").isEqualTo(2008)

    }

    @Test
    internal fun getAllMovies() {

        movieInfoService.saveMovieInfo(getMovieInfoKWithCast()).block()
        movieInfoService.saveMovieInfo(getMovieInfoKWithCast()).block()

        webTestClient.get().uri("/v1/movie_infos")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(MovieInfo::class.java)
            .hasSize(2)

    }

    @Test
    internal fun saveMovieInfo() {

        webTestClient.post().uri("/v1/movie_infos")
            .bodyValue(getMovieInfoKWithCast())
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.movieInfoId").isNotEmpty

    }

    @Test
    internal fun createMovieInfo_missingMovieName() {

        //given
        val movieInfo = getMovieInfoKWithCast()
        movieInfo.name = null
        //`when`(movieInfoServiceMock?.getMovieById(movieInfoId)).thenReturn(Flux.just(movieInfo))

        //when
        webTestClient.post().uri("/v1/movie_infos")
            .bodyValue(movieInfo)
            .exchange()
            .expectStatus().isBadRequest

    }

    @Test
    internal fun updateMovieInfo() {
        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo = movieInfoService.saveMovieInfo(movieInfo).block()
        savedMovieInfo?.cast?.add("Anna Hathaway")

        val exchangeResult =   webTestClient.put().uri("/v1/movie_infos/{id}",savedMovieInfo?.movieInfoId )
            .bodyValue(savedMovieInfo!!)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(MovieInfo::class.java)
            .returnResult()

        val movieInfoResult = exchangeResult.responseBody
        assertEquals(1,movieInfoResult?.size)
        movieInfoResult?.forEach {
            assertEquals(3, it.cast?.size)
        }
//        val castList = movieInfoResult?.cast
//        assertEquals(3, castList?.size)

    }

    @Test
    internal fun deleteMovieInfo() {

        movieInfoService.saveMovieInfo(getMovieInfoKWithCast()).block()
        movieInfoService.saveMovieInfo(getMovieInfoKWithCast()).block()

       val exchangeResult =  webTestClient.delete().uri("/v1/movie_infos")
            .exchange()
            .expectStatus().isOk
            .expectBody(Int::class.java)
            .returnResult()

        assertEquals(2, exchangeResult.responseBody)

    }



    @Test
    internal fun deleteMovieInfoById() {
        val movieInfo = movieInfoService.saveMovieInfo(getMovieInfoKWithCast()).block()

        val exchangeResult =  webTestClient.delete().uri("/v1/movie_infos/{id}", movieInfo?.movieInfoId)
            .exchange()
            .expectStatus().isOk
            .expectBody(Int::class.java)
            .returnResult()

        assertEquals(1, exchangeResult.responseBody)

    }
}