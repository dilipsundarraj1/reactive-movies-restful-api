package com.movies.service

import com.movies.exception.MovieInfoNotFoundException
import com.movies.exception.ReviewNotFoundException
import com.movies.util.getMovieInfoKWithCast
import com.movies.util.getReview
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import reactor.test.StepVerifier

@SpringBootTest
internal class ReviewServiceTestIT {

    @Autowired
    lateinit var movieInfoService: MovieInfoService

    @Autowired
    lateinit var reviewService: ReviewService

    @BeforeEach
    fun setUp() {
        reviewService.deleteAllReviews().block()
        movieInfoService.deleteAllMovieInfo().block()
    }

    @Test
    internal fun getAllReviews() {
        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo =  movieInfoService.saveMovieInfo(movieInfo).block()
        val review = getReview(savedMovieInfo!!)
        reviewService.saveReview(review).block()
        val review1 = getReview(savedMovieInfo)
        reviewService.saveReview(review1).block()


        StepVerifier.create(reviewService.getAllReviews())
            .expectNextCount(2)
            .verifyComplete()
    }

    @Test
    internal fun getReviewById() {
        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo =  movieInfoService.saveMovieInfo(movieInfo).block()
        val review = getReview(savedMovieInfo!!)
        val savedReview = reviewService.saveReview(review).block()

        val reviewFlux = reviewService.getReviewById(savedReview?.reviewId!!)

        StepVerifier.create(reviewFlux)
            .assertNext {
                println("movieInfo : $reviewFlux")
                assertNotNull(it.reviewId)
            }
            .verifyComplete()

    }

    @Test
    fun saveReview() {
        //given
        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo = movieInfoService.saveMovieInfo(movieInfo).block()
        val review = getReview(savedMovieInfo!!)

        //when
        val savedReview = reviewService.saveReview(review)

        //then
        StepVerifier.create(savedReview)
            .assertNext {
                println("Review is  : $it")
                assertNotNull(it.reviewId)
            }
            .verifyComplete()
    }

    @Test
    fun updateReview() {
        //given
        val newComment = "New Comment"
        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo = movieInfoService.saveMovieInfo(movieInfo).block()
        val review = getReview(savedMovieInfo!!)
        var savedReview = reviewService.saveReview(review).block()!!
        savedReview.comment = newComment

        //when
        val updatedReview = reviewService.updateReview(savedReview.reviewId!!, savedReview)

        //then
        StepVerifier.create(updatedReview)
            .assertNext {
                println("Updated Review is  : $it")
                assertNotNull(it.reviewId)
                assertEquals(newComment, it.comment)
            }
            .verifyComplete()
    }

    @Test
    fun updateReview_ErrorScenario() {
        //given
        val newComment = "New Comment"
        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo = movieInfoService.saveMovieInfo(movieInfo).block()
        val review = getReview(savedMovieInfo!!)
        var savedReview = reviewService.saveReview(review).block()!!
        savedReview.comment = newComment

        //when
        val updatedReview = reviewService.updateReview(100, savedReview)

        //then
        StepVerifier.create(updatedReview)
            .expectError(ReviewNotFoundException::class.java)
            .verify()
    }


    @Test
    internal fun deleteReviews() {
        //given
        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo = movieInfoService.saveMovieInfo(movieInfo).block()
        val review = getReview(savedMovieInfo!!)
        reviewService.saveReview(review).block()
        reviewService.saveReview(review).block()

        //when
        val deletedReviews = reviewService.deleteAllReviews()
        
        //then
        StepVerifier.create(deletedReviews)
            .assertNext {
                assertEquals(2,it)
            }
            .verifyComplete()


    }

    @Test
    internal fun deleteReviewById() {
        //given
        val movieInfo = getMovieInfoKWithCast()
        val savedMovieInfo = movieInfoService.saveMovieInfo(movieInfo).block()
        val review = getReview(savedMovieInfo!!)
        val savedReview = reviewService.saveReview(review).block()

        //when
        val deletedReview = reviewService.deleteReviewById(savedReview?.reviewId!!)

        //then
        StepVerifier.create(deletedReview)
            .assertNext {
                assertEquals(1,it)
            }
            .verifyComplete()


    }
}