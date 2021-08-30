package com.movies.util

import com.movies.domain.MovieInfo
import com.movies.domain.Review
import java.time.LocalDate

fun getMovieInfoK() = MovieInfo(null, "The Dark Knight", 2008, null, LocalDate.parse("2008-07-18"))
fun getMovieInfoKWithCast(movieInfoId : Long?=null) = MovieInfo(movieInfoId, "The Dark Knight", 2008, mutableListOf("Christian Bale","Tom Hardy"), LocalDate.parse("2008-07-18"))
fun getReview(movieInfo: MovieInfo, rating: Double?  = 8.9) = Review(null,movieInfo.movieInfoId!!,rating, "Awesome Movie")
