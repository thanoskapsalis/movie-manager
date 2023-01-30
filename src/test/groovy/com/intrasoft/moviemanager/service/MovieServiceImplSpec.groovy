package com.intrasoft.moviemanager.service

import com.intrasoft.moviemanager.dto.MovieDto
import com.intrasoft.moviemanager.dto.ReviewDto
import com.intrasoft.moviemanager.entity.Movie
import com.intrasoft.moviemanager.entity.Review
import com.intrasoft.moviemanager.exception.NotFoundException
import com.intrasoft.moviemanager.mapper.MovieMapper
import com.intrasoft.moviemanager.repository.MovieRepository
import com.intrasoft.moviemanager.repository.ReviewRepository
import com.intrasoft.moviemanager.service.implementations.MovieServiceImpl
import com.intrasoft.moviemanager.service.implementations.ReviewServiceImpl
import spock.lang.Specification

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
        reviewService = Mock()
        movieService = new MovieServiceImpl(movieRepository)

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
        result instanceof Movie;
        noExceptionThrown()
    }

    def "test getMovie throws NotFoundException"() {
        given:
        Movie movie = MovieMapper.INSTANCE.movieDtoToMovie(movieDto)
        1 * movieRepository.findById(1) >> Optional.empty()

        when:
        def result = movieService.getMovie(1)

        then:
        thrown(NotFoundException)
    }

    def "test updateMovie"() {
        given:
        Movie movie = MovieMapper.INSTANCE.movieDtoToMovie(movieDto)
        1 * movieRepository.findById(1) >> Optional.of(movie)
        1 * movieRepository.save(_) >> movie

        when:
        def result = movieService.updateMovie(movieDto.getId(), movieDto)

        then:
        result instanceof MovieDto
        noExceptionThrown()

    }

    def "test deleteMovie"() {
        given:
        Long id = 1

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

