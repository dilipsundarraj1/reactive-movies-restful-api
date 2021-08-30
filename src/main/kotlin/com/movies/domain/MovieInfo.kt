package com.movies.domain

import org.springframework.data.annotation.Id
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class MovieInfo(
    @Id
    var movieInfoId: Long? = null,
    @field:NotBlank(message = "Movie name is required")
    var name: String? = null,
    @field:NotNull(message = "Year of the movie is required")
    var year: Int? = null,
    @field:Size(min = 1)
    @Valid
    var cast: MutableList<String?>? = null,
    var release_date: LocalDate? = null) {
}