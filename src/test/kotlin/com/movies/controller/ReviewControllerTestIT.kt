package com.movies.controller

import com.movies.domain.MovieInfo
import com.movies.domain.Review
import com.movies.service.MovieInfoService
import com.movies.service.ReviewService
import com.movies.util.getMovieInfoKWithCast
import com.movies.util.getReview
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
class ReviewControllerTestIT {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var reviewService: ReviewService


    @Autowired
    lateinit var movieInfoService: MovieInfoService

    @BeforeEach
    internal fun setUp() {
        reviewService.deleteAllReviews().block()
        movieInfoService.deleteAllMovieInfo()
            .block()
    }

    @Test
    internal fun getReviewById() {

        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo =  movieInfoService.saveMovieInfo(movieInfo).block()
        val review = getReview(savedMovieInfo!!)
        val savedReview = reviewService.saveReview(review).block()

        webTestClient.get().uri("/v1/reviews/{review_id}", savedReview?.reviewId)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].rating").isEqualTo(8.9)
            .jsonPath("$[0].comment").isEqualTo("Awesome Movie")

    }

    @Test
    internal fun getReviewByMovieInfoId() {
        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo =  movieInfoService.saveMovieInfo(movieInfo).block()
        val review = getReview(savedMovieInfo!!)
        reviewService.saveReview(review).block()

        val uri = UriComponentsBuilder.fromUriString("/v1/reviews")
            .queryParam("movieInfoId", savedMovieInfo.movieInfoId)
            .buildAndExpand()
            .toUriString()

        webTestClient.get().uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].rating").isEqualTo(8.9)
            .jsonPath("$[0].comment").isEqualTo("Awesome Movie")


    }

    @Test
    internal fun getAllReviews() {

        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo =  movieInfoService.saveMovieInfo(movieInfo).block()
        val review = getReview(savedMovieInfo!!)
        reviewService.saveReview(review).block()
        reviewService.saveReview(review).block()

        webTestClient.get().uri("/v1/reviews")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Review::class.java)
            .hasSize(2)

    }

    @Test
    internal fun saveReview() {

        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo =  movieInfoService.saveMovieInfo(movieInfo).block()
        val review = getReview(savedMovieInfo!!)

        webTestClient.post().uri("/v1/reviews")
            .bodyValue(review)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.reviewId").isNotEmpty

    }

    @Test
    internal fun updateMovieInfo() {
        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo = movieInfoService.saveMovieInfo(movieInfo).block()
        val review = getReview(savedMovieInfo!!)
        var savedReview = reviewService.saveReview(review).block()
        savedReview?.rating = 9.1
        savedReview?.comment = "Best Movie"


        val exchangeResult = webTestClient.put().uri("/v1/reviews/{id}", savedReview?.reviewId)
                .bodyValue(savedReview!!)
                .exchange()
                .expectStatus().isOk
                .expectBodyList(Review::class.java)
                .returnResult()

        val reviewResult = exchangeResult.responseBody
        Assertions.assertEquals(1, reviewResult?.size)
        reviewResult?.forEach {
            Assertions.assertEquals(9.1, it.rating)
            Assertions.assertEquals("Best Movie", it.comment)
        }
    }

    @Test
    internal fun deleteReviewId() {
        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo =  movieInfoService.saveMovieInfo(movieInfo).block()
        val review = getReview(savedMovieInfo!!)
        val savedReview = reviewService.saveReview(review).block()

        val exchangeResult =  webTestClient.delete().uri("/v1/reviews/{id}", savedReview?.reviewId)
            .exchange()
            .expectStatus().isOk
            .expectBody(Int::class.java)
            .returnResult()

        Assertions.assertEquals(1, exchangeResult.responseBody)

    }

    @Test
    internal fun deleteMovieInfo() {

        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo =  movieInfoService.saveMovieInfo(movieInfo).block()
        val review = getReview(savedMovieInfo!!)
        reviewService.saveReview(review).block()
        reviewService.saveReview(review).block()

        val exchangeResult =  webTestClient.delete().uri("/v1/reviews")
            .exchange()
            .expectStatus().isOk
            .expectBody(Int::class.java)
            .returnResult()

        Assertions.assertEquals(2, exchangeResult.responseBody)

    }



}