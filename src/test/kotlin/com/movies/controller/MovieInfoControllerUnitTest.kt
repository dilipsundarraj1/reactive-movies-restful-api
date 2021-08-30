package com.movies.controller

import com.movies.domain.MovieInfo
import com.movies.exception.MovieInfoNotFoundException
import com.movies.service.MovieInfoService
import com.movies.util.getMovieInfoKWithCast
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.mockito.Mockito.*
import org.springframework.test.context.ActiveProfiles
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@ExtendWith(SpringExtension::class)
@WebFluxTest(MovieInfoController::class)
@ActiveProfiles("test")
class MovieInfoControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockBean
    private val movieInfoServiceMock: MovieInfoService? = null

    @Test
    internal fun getMovieById() {

        //given
        val movieInfoId: Long = 1
        val movieInfo = getMovieInfoKWithCast(movieInfoId)
        `when`(movieInfoServiceMock?.getMovieById(movieInfoId)).thenReturn(Mono.just(movieInfo))

        //when
        webTestClient.get().uri("/v1/movie_infos/{id}", movieInfoId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.name").isEqualTo("The Dark Knight")
            .jsonPath("$.year").isEqualTo(2008)
            .jsonPath("$.movieInfoId").isEqualTo(movieInfoId)

    }

    @Test
    internal fun createMovieInfo_missingMovieName() {

        //given
        val movieInfo = getMovieInfoKWithCast()
        movieInfo.name = null
        //`when`(movieInfoServiceMock?.getMovieById(movieInfoId)).thenReturn(Flux.just(movieInfo))

        //when
        val responseBodyList = webTestClient.post().uri("/v1/movie_infos")
            .bodyValue(movieInfo)
            .exchange()
            .expectStatus().isBadRequest
            .expectBodyList(String::class.java)
            .returnResult()

        val responseList = responseBodyList.responseBody!!
        assertEquals(1, responseList.size)
        assertEquals("[\"Movie name is required\"]", responseList[0])

    }

    @Test
    internal fun createMovieInfo_missingAllParams() {

        //given
        val movieInfo = MovieInfo(null, null, null, null, null)
        //`when`(movieInfoServiceMock?.getMovieById(movieInfoId)).thenReturn(Flux.just(movieInfo))

        //when
        val responseBodyList = webTestClient.post().uri("/v1/movie_infos")
            .bodyValue(movieInfo)
            .exchange()
            .expectStatus().isBadRequest
            .expectBodyList(String::class.java)
            .returnResult()

        val responseList = responseBodyList.responseBody!!
        responseList.forEach {
            println("it : $it")
        }
        assertEquals(1, responseList.size)
        /*assertEquals("[\"Movie name is required\"]", responseList[0])*/

    }


    @Test
    internal fun getMovieById_Exception() {

        //given
        val movieInfoId: Long = 1
        val errorMessage="Movie Not found for the given id"
        `when`(movieInfoServiceMock?.getMovieById(movieInfoId)).thenThrow(
            MovieInfoNotFoundException(
                errorMessage,
                null
            )
        );

        //when
        val response = webTestClient.get().uri("/v1/movie_infos/{id}", movieInfoId)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()

        assertEquals(errorMessage,response.responseBody)


    }
}