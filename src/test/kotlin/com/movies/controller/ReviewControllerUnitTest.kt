package com.movies.controller

import com.movies.service.ReviewService
import com.movies.util.getMovieInfoKWithCast
import com.movies.util.getReview
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@WebFluxTest(ReviewController::class)
class ReviewControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var reviewServiceMock: ReviewService

    @Test
    internal fun createNewReview() {
        val movieInfo = getMovieInfoKWithCast(1)
        val review = getReview(movieInfo, null)
        review.movieInfoId = null

        val responseList =  webTestClient.post().uri("/v1/reviews")
                .bodyValue(review)
                .exchange()
                .expectStatus().isBadRequest
                .expectBodyList(String::class.java)
                .returnResult()
                .responseBody!!

        Assertions.assertEquals(1, responseList.size)
        Assertions.assertEquals("[\"MovieInfo Id is required\",\"Rating is required\"]", responseList[0])
    }
}