package com.movies.controller

import com.movies.domain.MovieInfo
import com.movies.domain.Review
import com.movies.service.ReviewService
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid

@RestController
class ReviewController(val reviewService: ReviewService) {

    companion object : KLogging()

    @GetMapping("/v1/reviews")
    fun getAllReviews(@RequestParam("movieInfoId", required = false) movieInfoId: Long?): Flux<Review> {
        if (movieInfoId != null) {
            return reviewService.getReviewByMovieInfoId(movieInfoId)
                .doOnNext {
                    logger.info { "Review is $it" }
                }
        } else
            return reviewService.getAllReviews()
                .doOnNext {
                    logger.info { "Review is $it" }
                }
    }

    @GetMapping("/v1/reviews/{review_id}")
    fun getReviewById(@PathVariable("review_id") reviewId: Long): Flux<Review> {
        return reviewService.getReviewById(reviewId)
    }

    /*   @GetMapping("/v1/reviews")
       fun getReviewByMovieInfo( @RequestParam("movieInfoId") movieInfoId : Long): Flux<Review> {
           return reviewService.getReviewByMovieInfoId(movieInfoId)
       }*/

    @PostMapping("/v1/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    fun createReview(@RequestBody @Valid review: Review): Mono<Review> {
        return reviewService.saveReview(review)
    }

    @PutMapping("/v1/reviews/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateReview(@PathVariable("id") reviewId: Long, @RequestBody review: Review): Flux<Review> {
        return reviewService.updateReview(reviewId, review)
    }


    @DeleteMapping("/v1/reviews/{id}")
    fun deleteReviewById(@PathVariable("id") id: Long): Mono<Int> {
        return reviewService.deleteReviewById(id)
    }

    @DeleteMapping("/v1/reviews")
    fun deleteAllReviews(): Mono<Int> {
        return reviewService.deleteAllReviews()
    }

}