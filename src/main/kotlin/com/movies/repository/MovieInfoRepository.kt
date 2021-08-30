package com.movies.repository

import com.movies.domain.MovieInfo
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface MovieInfoRepository : ReactiveCrudRepository<MovieInfo, Long> {
}