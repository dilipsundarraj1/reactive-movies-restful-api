package com.movies.service

import com.movies.domain.MovieInfo
import com.movies.domain.Review
import com.movies.exception.MovieInfoNotFoundException
import com.movies.exception.ReviewNotFoundException
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ReviewService(val r2dbcEntityTemplate: R2dbcEntityTemplate) {


    fun getAllReviews(): Flux<Review> {
        return r2dbcEntityTemplate.select(Review::class.java)
            .all()
    }

    fun getReviewById(review_Id : Long): Flux<Review> {
        return r2dbcEntityTemplate.select(Query.query(where("review_Id").`is`(review_Id)),Review::class.java)
            .log()
    }

    fun getReviewByMovieInfoId(movieInfoId: Long): Flux<Review> {
        return r2dbcEntityTemplate.select(Query.query(where("movie_info_id").`is`(movieInfoId)),Review::class.java)

    }

    fun saveReview(review: Review) : Mono<Review> {
        return r2dbcEntityTemplate.insert(review)
    }

    fun deleteReviewById(reviewId: Long): Mono<Int> {
        return r2dbcEntityTemplate.delete(Review::class.java)
            .from("REVIEW")
            .matching(Query.query(where("review_id").`is`(reviewId)))
            .all()
    }

    fun updateReview(reviewId: Long, review: Review): Flux<Review> {
        val reviewFromDb =
            r2dbcEntityTemplate.select(Query.query(where("review_Id").`is`(reviewId)), Review::class.java)
        return reviewFromDb.switchIfEmpty(
            Mono.error(ReviewNotFoundException("Review not found with the passed id", null))
        ).flatMap {
            r2dbcEntityTemplate.update(review)
        }

    }

    fun deleteAllReviews(): Mono<Int> {
        return r2dbcEntityTemplate.delete(Review::class.java)
            .from("REVIEW")
            .all()
    }


}