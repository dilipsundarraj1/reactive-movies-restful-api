package com.movies.controller

import com.movies.domain.MovieInfo
import com.movies.service.MovieInfoService
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid

@RestController
@RequestMapping("/v1")
class MovieInfoController(val movieInfoService: MovieInfoService) {

    companion object : KLogging()

    @GetMapping("/movie_infos/{id}")
    fun movieInfoById(@PathVariable("id") movieInfoId: Long): Mono<MovieInfo> {
        return movieInfoService.getMovieById(movieInfoId)
            .doOnNext {
                logger.info { "Movie is $it" }
            }
    }

    @GetMapping("/movie_infos")
    fun movieInfos(): Flux<MovieInfo> {
        return movieInfoService.getAllMovies()
            .doOnNext {
                logger.info { "Movie is $it" }
            }
    }

    @PostMapping("/movie_infos")
    @ResponseStatus(HttpStatus.CREATED)
    fun createMovieInfo(@RequestBody @Valid movieInfo: MovieInfo): Mono<MovieInfo> {
        logger.info("movieInfo : $movieInfo")
        return movieInfoService.saveMovieInfo(movieInfo)
    }

    @PutMapping("/movie_infos/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateMovieInfo(@PathVariable("id") movieInfoId: Long, @RequestBody movieInfo: MovieInfo): Flux<MovieInfo> {
        return movieInfoService.updateMovieInfo(movieInfoId, movieInfo)
    }


    @DeleteMapping("/movie_infos/{id}")
    fun deleteMovieInfo(@PathVariable("id") id: Long): Mono<Int> {
        return movieInfoService.deleteMovieInfo(id)
    }

    @DeleteMapping("/movie_infos")
    fun deleteAllMovieInfo(): Mono<Int> {
        return movieInfoService.deleteAllMovieInfo()
    }
}