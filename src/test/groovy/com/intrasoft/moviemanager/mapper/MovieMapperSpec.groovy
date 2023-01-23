package com.intrasoft.moviemanager.mapper

import com.intrasoft.moviemanager.dto.MovieDto
import com.intrasoft.moviemanager.dto.ReviewDto
import com.intrasoft.moviemanager.entity.Movie
import com.intrasoft.moviemanager.entity.Review
import org.codehaus.groovy.runtime.wrappers.PojoWrapper
import spock.lang.Specification

import java.time.LocalDateTime

class MovieMapperSpec extends Specification {
    ReviewDto reviewDto;
    MovieDto movieDto;

    Review review;
    Movie movie;

    def setup() {


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
    }


    def "test movieToMovieDto Object"() {
        when:
        def result = MovieMapper.INSTANCE.movieToMovieDto(movie)

        then:
        result instanceof MovieDto
    }


    def "test movieToMovieDto List"() {
        given:
        Movie movie2 = Movie.builder()
                .movieName("Sample name")
                .id(1)
                .description("test")
                .timeCreated(LocalDateTime.now())
                .movieReviews(new ArrayList<Review>())
                .build()

        List<Movie> movies = new ArrayList<>()
        movies.add(movie)
        movies.add(movie2)

        when:
        def result = MovieMapper.INSTANCE.movieToMovieDto(movies)

        then:
        result instanceof List<MovieDto>
    }

    def "test movieDtoToMovie Object"() {
        when:
        def result = MovieMapper.INSTANCE.movieDtoToMovie(movieDto)

        then:
        result instanceof Movie
    }

    def "test movieDtoToMovie List"() {
        given:
        MovieDto movieDto2 = MovieDto.builder()
                .movieName("Sample name")
                .id(1)
                .description("test")
                .timeCreated(LocalDateTime.now())
                .movieReviews(new ArrayList<Review>())
                .build()

        List<MovieDto> movieDtos = new ArrayList<>()
        movieDtos.add(movieDto)
        movieDtos.add(movieDto2)

        when:
        def result = MovieMapper.INSTANCE.movieDtoToMovie(movieDtos)

        then:
        result instanceof List<Movie>
    }

    def "test movieToMovieDto return null"() {
        when:
        def input = new PojoWrapper(null, Movie)
        def result = MovieMapper.INSTANCE.movieToMovieDto(null)
        MovieDto movieDtoNull = MovieMapper.INSTANCE.movieToMovieDto(input)

        then:
        result == null
        movieDtoNull == null
    }

    def "test movieDtoToMovie return null"() {
        when:
        def input = new PojoWrapper(null, MovieDto)
        def result = MovieMapper.INSTANCE.movieDtoToMovie(null)
        Movie movieNull = MovieMapper.INSTANCE.movieDtoToMovie(input)

        then:
        result == null
        movieNull == null
    }
}