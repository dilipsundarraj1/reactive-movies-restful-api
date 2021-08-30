package com.movies.domain

import org.springframework.data.annotation.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class Review(
    @Id
    val reviewId: Long?=null,
    @field:NotNull(message = "MovieInfo Id is required")
        var movieInfoId: Long?,
    @field:NotNull(message = "Rating is required")
        var rating: Double?,
    var comment: String? = null

)
