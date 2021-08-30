package com.movies.service

import com.movies.exception.MovieInfoNotFoundException
import com.movies.repository.MovieInfoRepository
import com.movies.util.getMovieInfoK
import com.movies.util.getMovieInfoKWithCast
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.jdbc.Sql
import reactor.test.StepVerifier
import java.lang.Thread.sleep

@SpringBootTest
@DirtiesContext
private class MovieInfoServiceTestIT {

    @Autowired
    lateinit var movieInfoService: MovieInfoService

    @Autowired
    lateinit var reviewService: ReviewService


    @AfterEach
    internal fun tearDown() {
        reviewService.deleteAllReviews().block()
        movieInfoService.deleteAllMovieInfo()
            .block()
    }

    @Test
    internal fun getAllMovieInfo() {
        val movieInfo = getMovieInfoKWithCast()
        movieInfoService.saveMovieInfo(movieInfo).block()
        val movieInfo1 = getMovieInfoKWithCast()
        movieInfoService.saveMovieInfo(movieInfo1).block()

        StepVerifier.create(movieInfoService.getAllMovies())
            .expectNextCount(2)
            .verifyComplete()
    }

    @Test
    internal fun getMovieInfoById() {
        val movieInfo = getMovieInfoKWithCast()
        movieInfo.cast?.add("Anna Hathaway")
        val savedMovieInfo =movieInfoService.saveMovieInfo(movieInfo).block()

        val movieFlux = movieInfoService.getMovieById(savedMovieInfo?.movieInfoId!!)

        StepVerifier.create(movieFlux)
            .assertNext {
                println("movieInfo : $movieInfo")
                assertNotNull(it.movieInfoId)
            }
            .verifyComplete()

    }

    @Test
    internal fun saveMovieInfoK() {
        val movieInfo = getMovieInfoK()
        val savedMovieInfo = movieInfoService.saveMovieInfo(movieInfo)
        StepVerifier.create(savedMovieInfo)
            .assertNext {
                assertNotNull(it.movieInfoId)
            }
            .verifyComplete()
    }

    @Test
    internal fun saveMovieInfoK_withCast() {
        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo = movieInfoService.saveMovieInfo(movieInfo)
        StepVerifier.create(savedMovieInfo)
            .assertNext {
                println("movieInfo : $movieInfo")
                assertNotNull(it.movieInfoId)
            }
            .verifyComplete()

        sleep(2000)
    }

    @Test
    internal fun updateMovieInfo() {
        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo = movieInfoService.saveMovieInfo(movieInfo).block()
        savedMovieInfo?.cast?.add("Anna Hathaway")
        val updatedMovieInfo = movieInfoService.updateMovieInfo(savedMovieInfo?.movieInfoId!!, savedMovieInfo)
        StepVerifier.create(updatedMovieInfo)
            .assertNext {
                println("movieInfo : $movieInfo")
                assertNotNull(it.movieInfoId)
                assertEquals(3, it.cast?.size)
            }
            .verifyComplete()
    }

    @Test
    internal fun updateMovieInfo_withInvalidId() {
        val movieInfo = getMovieInfoKWithCast()
        movieInfo.movieInfoId = 100
        movieInfo?.cast?.add("Anna Hathaway")
        val updatedMovieInfo = movieInfoService.updateMovieInfo(movieInfo?.movieInfoId!!, movieInfo)
        StepVerifier.create(updatedMovieInfo)
            .expectError(MovieInfoNotFoundException::class.java)
            .verify()
    }

    @Test
    internal fun deleteMovieInfo() {
        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo = movieInfoService.saveMovieInfo(movieInfo).block()
        println("savedMovieInfo : $savedMovieInfo")
        val result = movieInfoService.deleteMovieInfo(savedMovieInfo?.movieInfoId!!)
        StepVerifier.create(result)
            .assertNext {
                println("Deleted MovieInfo id  : $it")
                assertEquals(1,it.toLong())
            }
            .verifyComplete()


    }
}