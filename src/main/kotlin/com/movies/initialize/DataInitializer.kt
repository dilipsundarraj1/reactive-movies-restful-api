package com.movies.initialize

import com.movies.domain.MovieInfo
import com.movies.domain.Review
import com.movies.service.MovieInfoService
import com.movies.service.ReviewService
import mu.KLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.LocalDate

@Component
class DataInitializer(
    val movieInfoService: MovieInfoService,
    val reviewService: ReviewService
) : CommandLineRunner {

    companion object : KLogging()

    override fun run(vararg args: String?) {
        val movieList = movieInfoData()
        createMovieInfosAndReviews(movieAndReviewDataSetUp())
    }

    private fun createMovieInfosAndReviews(movieAndReviewData: MutableMap<MovieInfo, Review>) {

       movieAndReviewData
            .map { map ->
                val savedMovieInfo = Mono.just(map.key)
                    .flatMap {
                        movieInfoService.saveMovieInfo(it)
                    }

                savedMovieInfo
                    .flatMap {
                        val movieInfoId = it.movieInfoId
                        var review = map.value
                        review.movieInfoId = movieInfoId
                        reviewService.saveReview(review)

                    }
                    .subscribe()

            }

        listMovieIds()
        movieInfoService.getAllMovies()
                    .count()
                  .subscribe {
                logger.info("No of persisted Movies are : $it")
            }
    }

    fun listMovieIds(){

        val movieInfoIdList  = movieInfoService.getAllMovies()
            .map { it::movieInfoId.get() }
            .collectList()

        movieInfoIdList.subscribe {
            logger.info("Persisted Movie Ids are : $it")
        }
    }

    private fun movieInfoData(): MutableList<MovieInfo> {
        return mutableListOf(
            MovieInfo(
                null,
                "Batman Begins",
                2005,
                mutableListOf("Christian Bale", "Michael Cane"),
                LocalDate.parse("2005-06-15")
            ),
            MovieInfo(
                null,
                "The Dark Knight",
                2008,
                mutableListOf("Christian Bale", "HeathLedger"),
                LocalDate.parse("2008-07-18")
            ),
            MovieInfo(
                null,
                "Dark Knight Rises",
                2008,
                mutableListOf("Christian Bale", "Tom Hardy"),
                LocalDate.parse("2012-07-20")
            ),
            MovieInfo(
                null, "The Avengers", 2012, mutableListOf(
                    "Robert Downey Jr.", "Chris Evans", "Mark Ruffalo", "Chris Hemsworth",
                    "Scarlett Johansson", "Jeremy Renner", "Tom Hiddleston", "Samuel L. Jackson"
                ), LocalDate.parse("2012-04-11")
            ),
            MovieInfo(
                null, "Avengers: Age of Ultron", 2015, mutableListOf(
                    "Robert Downey Jr.",
                    "Chris Evans",
                    "Mark Ruffalo",
                    "Chris Hemsworth",
                    "Scarlett Johansson",
                    "Jeremy Renner",
                    "Tom Hiddleston",
                    "Samuel L. Jackson",
                    "Don Cheadle",
                    "Idris Elba"
                ), LocalDate.parse("2015-04-13")
            ),
            MovieInfo(
                null, "Avengers: Infinity War", 2018, mutableListOf(
                    "Robert Downey Jr.",
                    "Chris Evans",
                    "Mark Ruffalo",
                    "Chris Hemsworth",
                    "Scarlett Johansson",
                    "Jeremy Renner",
                    "Tom Hiddleston",
                    "Samuel L. Jackson",
                    "Don Cheadle",
                    "Idris Elba",
                    "Chris Patt",
                    "Elizabeth Olsen",
                    "Dave Bautista",
                    "Benedict Cumberbatch",
                    "Tom Holland",
                    "Chadwick Boseman"
                ), LocalDate.parse("2018-04-23")
            ),
            MovieInfo(
                null, "Avengers: Infinity War", 2019, mutableListOf(
                    "Robert Downey Jr.",
                    "Chris Evans",
                    "Mark Ruffalo",
                    "Chris Hemsworth",
                    "Scarlett Johansson",
                    "Jeremy Renner",
                    "Tom Hiddleston",
                    "Samuel L. Jackson",
                    "Don Cheadle",
                    "Idris Elba",
                    "Chris Patt",
                    "Elizabeth Olsen",
                    "Dave Bautista",
                    "Benedict Cumberbatch",
                    "Tom Holland",
                    "Chadwick Boseman",
                    "Gwyneth Paltrow",
                    "Brie Larson"
                ), LocalDate.parse("2019-04-22")
            )
        )

    }

    private fun movieAndReviewDataSetUp(): MutableMap<MovieInfo, Review> {

        return mutableMapOf(
            MovieInfo(
                null,
                "Batman Begins",
                2005,
                mutableListOf("Christian Bale", "Michael Cane"),
                LocalDate.parse("2005-06-15")
            ) to Review(null, null, 8.2, "Nolan is the real superhero"),
            MovieInfo(
                null,
                "The Dark Knight",
                2008,
                mutableListOf("Christian Bale", "HeathLedger"),
                LocalDate.parse("2008-07-18")
            ) to Review(null, null, 9.0, "MASTERPIECE"),
            MovieInfo(
                null,
                "Dark Knight Rises",
                2008,
                mutableListOf("Christian Bale", "Tom Hardy"),
                LocalDate.parse("2012-07-20")
            ) to Review(null, null, 8.4, "EPIC"),
            MovieInfo(
                null, "The Avengers", 2012, mutableListOf(
                    "Robert Downey Jr.", "Chris Evans", "Mark Ruffalo", "Chris Hemsworth",
                    "Scarlett Johansson", "Jeremy Renner", "Tom Hiddleston", "Samuel L. Jackson"
                ), LocalDate.parse("2012-04-11")
            ) to Review(null, null, 8.0, "Avengers is an amazing movie with an amazing cast"),
            MovieInfo(
                null, "Avengers: Age of Ultron", 2015, mutableListOf(
                    "Robert Downey Jr.",
                    "Chris Evans",
                    "Mark Ruffalo",
                    "Chris Hemsworth",
                    "Scarlett Johansson",
                    "Jeremy Renner",
                    "Tom Hiddleston",
                    "Samuel L. Jackson",
                    "Don Cheadle",
                    "Idris Elba"
                ), LocalDate.parse("2015-04-13")
            ) to Review(null, null, 7.3, "Avengers: Age of Ultron is a damn good Superhero flick, excellent sequel!"),
            MovieInfo(
                null, "Avengers: Infinity War", 2018, mutableListOf(
                    "Robert Downey Jr.",
                    "Chris Evans",
                    "Mark Ruffalo",
                    "Chris Hemsworth",
                    "Scarlett Johansson",
                    "Jeremy Renner",
                    "Tom Hiddleston",
                    "Samuel L. Jackson",
                    "Don Cheadle",
                    "Idris Elba",
                    "Chris Patt",
                    "Elizabeth Olsen",
                    "Dave Bautista",
                    "Benedict Cumberbatch",
                    "Tom Holland",
                    "Chadwick Boseman"
                ), LocalDate.parse("2018-04-23")
            ) to Review(null, null, 8.4, "Best Cliffhanger of all Time"),
            MovieInfo(
                null, "Avengers: Infinity War", 2019, mutableListOf(
                    "Robert Downey Jr.",
                    "Chris Evans",
                    "Mark Ruffalo",
                    "Chris Hemsworth",
                    "Scarlett Johansson",
                    "Jeremy Renner",
                    "Tom Hiddleston",
                    "Samuel L. Jackson",
                    "Don Cheadle",
                    "Idris Elba",
                    "Chris Patt",
                    "Elizabeth Olsen",
                    "Dave Bautista",
                    "Benedict Cumberbatch",
                    "Tom Holland",
                    "Chadwick Boseman",
                    "Gwyneth Paltrow",
                    "Brie Larson"
                ), LocalDate.parse("2019-04-22")
            ) to Review(null, null, 8.4, "The ending made all 22 movies worth it"),
        )

    }

}