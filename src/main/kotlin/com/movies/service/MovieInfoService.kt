package com.movies.service

import com.movies.domain.Cast
import com.movies.domain.MovieInfo
import com.movies.exception.MovieInfoNotFoundException
import mu.KLogging
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Query.query
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.error

@Service
class MovieInfoService(val r2dbcEntityTemplate: R2dbcEntityTemplate) {

    companion object : KLogging() {
    }

    fun getAllMovies(): Flux<MovieInfo> {
        return r2dbcEntityTemplate.select(MovieInfo::class.java)
            .all()
            .sort(compareBy {
                it.movieInfoId
            })
    }

    fun getMovieById(movieInfoId: Long): Mono<MovieInfo> {
        return r2dbcEntityTemplate.select(query(where("movie_info_id").`is`(movieInfoId)), MovieInfo::class.java)
            .collectList()
            .flatMap {
                Mono.just(it[0])
            }
    }


    fun saveMovieInfo(movieInfo: MovieInfo): Mono<MovieInfo> {
        return r2dbcEntityTemplate.insert(movieInfo)
    }

    fun updateMovieInfo(movieInfoId: Long, movieInfo: MovieInfo): Flux<MovieInfo> {
        val movieInfoFromDb =
            r2dbcEntityTemplate.select(query(where("movie_info_id").`is`(movieInfoId)), MovieInfo::class.java)
        return movieInfoFromDb.switchIfEmpty(
            error(MovieInfoNotFoundException("MovieInfo not found with the passed id", null))
        ).flatMap {
            r2dbcEntityTemplate.update(movieInfo)
        }

    }


    fun deleteMovieInfo(movieInfoId: Long): Mono<Int> {
        return r2dbcEntityTemplate.delete(MovieInfo::class.java)
            .from("MOVIE_INFO")
            .matching(query(where("movie_info_id").`is`(movieInfoId)))
            .all()
    }

    fun deleteAllMovieInfo(): Mono<Int> {
        return r2dbcEntityTemplate.delete(MovieInfo::class.java)
            .from("MOVIE_INFO")
            .all()
    }

}