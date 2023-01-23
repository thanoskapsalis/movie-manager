package com.intrasoft.moviemanager.service

import com.intrasoft.moviemanager.dto.MovieDto
import com.intrasoft.moviemanager.dto.ReviewDto
import com.intrasoft.moviemanager.entity.Movie
import com.intrasoft.moviemanager.entity.Review
import com.intrasoft.moviemanager.mapper.MovieMapper
import com.intrasoft.moviemanager.mapper.ReviewMapper
import com.intrasoft.moviemanager.repository.MovieRepository
import com.intrasoft.moviemanager.repository.ReviewRepository
import com.intrasoft.moviemanager.service.implementations.MovieServiceImpl
import com.intrasoft.moviemanager.service.implementations.ReviewServiceImpl
import spock.lang.Specification

import java.nio.file.NoSuchFileException
import java.time.LocalDateTime

class MovieServiceImplSpec extends Specification {

    MovieRepository movieRepository
    ReviewRepository reviewRepository
    ReviewServiceImpl reviewService
    MovieServiceImpl movieService

    ReviewDto reviewDto;
    MovieDto movieDto;

    Review review;
    Movie movie;

    def setup() {
        movieRepository = Mock()
        reviewRepository = Mock()
        reviewService = new ReviewServiceImpl(reviewRepository)
        movieService = new MovieServiceImpl(movieRepository, reviewService)

        reviewDto = ReviewDto.builder()
                .id(1)
                .reviewerName("reviewName")
                .reviewContent("reviewContent")
                .reviewTitle("reviewTitle")
                .build()

        movie = Movie.builder()
                .movieName("Sample name")
                .id(1)
                .description("test")
                .timeCreated(LocalDateTime.now())
                .movieReviews(new ArrayList<Review>())
                .build()

        movieDto = MovieDto.builder()
                .movieName("Sample movie")
                .description("movie description")
                .id(1)
                .build()

        review = Review.builder()
                .reviewTitle("reviewTitle")
                .reviewContent("reviewContent")
                .reviewerName("reviewName")
                .id(1)
                .build()
    }

    def "test createMovie"() {
        given:
        1 * movieRepository.save(_) >> movie

        when:
        def result = movieService.createMovie(MovieMapper.INSTANCE.movieToMovieDto(movie))

        then:
        result instanceof MovieDto
    }

    def "test getMovie success"() {
        given:
        Movie movie = MovieMapper.INSTANCE.movieDtoToMovie(movieDto)
        1 * movieRepository.findById(1) >> Optional.of(movie)

        when:
        def result = movieService.getMovie(1)

        then:
        result == movieDto
        noExceptionThrown()
    }

    def "test getMovie throws NoSuchFileException"() {
        given:
        Movie movie = MovieMapper.INSTANCE.movieDtoToMovie(movieDto)
        1 * movieRepository.findById(1) >> Optional.empty()

        when:
        def result = movieService.getMovie(1)

        then:
        thrown(NoSuchFileException)
    }

    def "test updateMovie"() {
        given:
        Movie movie = MovieMapper.INSTANCE.movieDtoToMovie(movieDto)
        1 * movieRepository.findById(1) >> Optional.of(movie)
        1 * movieRepository.save(_) >> movie

        when:
        def result = movieService.updateMovie(movieDto.getId(), movieDto)

        then:
        result == movieDto
        noExceptionThrown()

    }

    def "test deleteMovie"() {
        given:
        Long id = 1
        1 * movieRepository.findById(1) >> Optional.of(movie)

        when:
        def result = movieService.deleteMovie(1)

        then:
        result == null
    }

    def "test getMovies"() {
        given:
        1 * movieRepository.findAll() >> Collections.emptyList()

        when:
        def result = movieService.getMovies()

        then:
        result instanceof List<MovieDto>
    }

    def "test reviewMovie"() {
        given:

        ReviewDto reviewDtoWithMovie = ReviewDto.builder()
                .id(1)
                .reviewerName("reviewName")
                .reviewContent("reviewContent")
                .reviewTitle("reviewTitle")
                .movie(movie)
                .build()

        Review review = ReviewMapper.INSTANCE.reviewDtoToReview(reviewDtoWithMovie)

        Long id = 1;
        1 * movieRepository.findById(1) >> Optional.of(movie)
        1 * reviewRepository.save(_) >> review

        when:
        def result = movieService.reviewMovie(id, reviewDtoWithMovie)

        then:
        result instanceof ReviewDto
    }

    def "test deleteMovieReview with no reviews"() {
        given:
        Long movieId = 1
        Long reviewId = 1

        1 * movieRepository.save(_) >> movie
        1 * movieRepository.findById(_) >> Optional.of(movie)

        when:
        def result = movieService.deleteMovieReview(movieId, reviewId)

        then:
        noExceptionThrown()
    }

    def "test deleteMovieReview with a review"() {
        given:
        Long movieId = 1
        Long reviewId = 1

        Movie movieWithReview = Movie.builder()
                .movieName("Sample name")
                .id(2)
                .description("test")
                .timeCreated(LocalDateTime.now())
                .movieReviews(new ArrayList<Review>())
                .build()

        1 * movieRepository.save(_) >> movie
        1 * movieRepository.findById(_) >> Optional.of(movie)

        when:
        movieWithReview.getMovieReviews().add(review)
        def result = movieService.deleteMovieReview(movieId, reviewId)

        then:
        noExceptionThrown()
    }


    def "test updateMovieReview"() {
        given:
        Long movieId = 1
        Long reviewId = 1

        Movie movie2 = Movie.builder()
                .movieName("Sample name")
                .id(1)
                .description("test")
                .timeCreated(LocalDateTime.now())
                .movieReviews(new ArrayList<Review>())
                .build()

        Review reviewWithMovie = Review.builder()
                .reviewTitle("reviewTitle")
                .reviewContent("reviewContent")
                .reviewerName("reviewName")
                .id(1)
                .movie(movie2)
                .build()

        ReviewDto reviewWithMovieDto = ReviewMapper.INSTANCE.reviewToReviewDto(reviewWithMovie)

        1 * movieRepository.findById(1) >> Optional.of(movie2)
        1 * reviewService.updateReview(_, _) >> reviewDto

        when:
        movie2.getMovieReviews().add(reviewWithMovie)
        def result = movieService.updateMovieReview(movieId, reviewId, reviewWithMovieDto)

        then:
        result instanceof ReviewDto
    }

    def "test updateMovieReview throws noSuchFileException"() {
        given:
        Long movieId = 1
        Long reviewId = 1

        1 * movieRepository.findById(_) >> Optional.of(movie)

        when:
        def result = movieService.updateMovieReview(movieId, reviewId, reviewDto)

        then:
        thrown(NoSuchFileException)
    }

    def "test SearchMovie with filters"() {
        given:
        String movieName = movie.movieName
        String description = movie.description
        LocalDateTime timeCreated = movie.timeCreated


        4 * movieRepository.findAll(_) >> Collections.emptyList()

        when:
        def resultAll = movieService.searchMovies(movieName, description, timeCreated)
        def resultMovieNameNull = movieService.searchMovies(null, description, timeCreated)
        def resultDescriptionNull = movieService.searchMovies(movieName, null, timeCreated)
        def resultTimeCreatedNull = movieService.searchMovies(movieName, description, null)


        then:
        resultAll instanceof List<MovieDto>
        resultMovieNameNull instanceof List<MovieDto>
        resultDescriptionNull instanceof List<MovieDto>
        resultTimeCreatedNull instanceof List<MovieDto>

    }
}

