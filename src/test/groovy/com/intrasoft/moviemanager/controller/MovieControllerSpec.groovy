package com.intrasoft.moviemanager.controller

import com.intrasoft.moviemanager.dto.MovieDto
import com.intrasoft.moviemanager.dto.ReviewDto
import com.intrasoft.moviemanager.entity.Movie
import com.intrasoft.moviemanager.service.implementations.MovieServiceImpl
import org.springframework.http.HttpStatus
import spock.lang.Specification

import java.time.LocalDateTime

class MovieControllerSpec extends Specification {

    private MovieController controller;
    private MovieServiceImpl movieService;

    def setup() {
        movieService = Mock();
        controller = new MovieController(movieService);
    }

    def "ensure controller created"() {
        expect: "bean creation is successful"
        controller != null
    }

    def "test getMovies"() {
        when:
        def response = controller.getMovies()

        then:
        response.getStatusCode() == HttpStatus.OK
    }

    def "get a movie from the database"() {
        given:
        Long id = 1

        when:
        def response = controller.getMovie(id)

        then:
        response.getStatusCode() == HttpStatus.OK
    }

    def "test searchMovie"() {
        given:
        String movieName = "test"
        String description = "test_description"
        LocalDateTime timeCreated = LocalDateTime.now()

        def movieDtos = new ArrayList<MovieDto>()
        def movieDto = MovieDto.builder().build()
        movieDtos.add(movieDto)
        1 * movieService.searchMovies(movieName, description, timeCreated) >> movieDtos

        when:
        def response = controller.searchMovie(movieName, description, timeCreated)

        then:
        response.getStatusCode() == HttpStatus.OK
        response.getBody() instanceof List<MovieDto>

    }

    def "test searchMovie no keys found"() {
        given:
        String movieName = "test"
        String description = "test_description"
        LocalDateTime timeCreated = LocalDateTime.now()

        def movieDtos = new ArrayList<MovieDto>()
        1 * movieService.searchMovies(movieName, description, timeCreated) >> Collections.emptyList()

        when:
        def response = controller.searchMovie(movieName, description, timeCreated)

        then:
        response.getStatusCode() == HttpStatus.NO_CONTENT
    }

    def "test createMovie"() {
        given:
        def movieDto = MovieDto.builder().build()

        when:
        def response = controller.createNewMovie(movieDto);

        then:
        response.getStatusCode() == HttpStatus.OK

    }

    def "test createMovieReview"() {
        given:
        def reviewDto = ReviewDto.builder().build()
        Long id = 1

        when:
        def response = controller.createMovieReview(id, reviewDto);

        then:
        response.getStatusCode() == HttpStatus.OK

    }

    def "test updateMovie"() {
        given:
        def movieDto = MovieDto.builder().build()
        Long id = 1

        when:
        def response = controller.updateMovie(id, movieDto)

        then:
        response.getStatusCode() == HttpStatus.OK
    }

    def "test updateMovieReview"() {
        given:
        def reviewDto = ReviewDto.builder().build()
        Long id = 1

        when:
        def response = controller.updateMovieReview(id, id, reviewDto)

        then:
        response.getStatusCode() == HttpStatus.OK
    }

    def "test deleteMovie"() {
        given:
        Long id = 1

        when:
        def response = controller.deleteMovie(id)

        then:
        response.getStatusCode() == HttpStatus.OK
    }

    def "test deleteMovieReview"() {
        given:
        Long id = 1

        when:
        def response = controller.deleteMovieReview(id, id)

        then:
        response.getStatusCode() == HttpStatus.OK
    }

}