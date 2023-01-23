package com.intrasoft.moviemanager.service

import com.intrasoft.moviemanager.dto.MovieDto
import com.intrasoft.moviemanager.dto.ReviewDto
import com.intrasoft.moviemanager.entity.Movie
import com.intrasoft.moviemanager.entity.Review
import com.intrasoft.moviemanager.repository.MovieRepository
import com.intrasoft.moviemanager.repository.ReviewRepository
import com.intrasoft.moviemanager.service.implementations.MovieServiceImpl
import com.intrasoft.moviemanager.service.implementations.ReviewServiceImpl
import spock.lang.Specification

import java.nio.file.NoSuchFileException
import java.time.LocalDateTime

class ReviewServiceImpSpec extends Specification {

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

    def "test newReview"() {
        given:
        1 * reviewRepository.save(_) >> review

        when:
        def result = reviewService.createReview(reviewDto)

        then:
        result instanceof ReviewDto
    }

    def "test getReview"() {
        given:
        Long id = 1
        1 * reviewRepository.findById(_) >> Optional.of(review)

        when:
        def result = reviewService.getReview(1)

        then:
        result instanceof ReviewDto
    }

    def "test getReview throws NoSuchFileException"() {
        given:
        Long id = 1
        1 * reviewRepository.findById(_) >> Optional.empty()

        when:
        def result = reviewService.getReview(1)

        then:
        thrown(NoSuchFileException)
    }

    def "test getReviews"() {
        given:
        1 * reviewRepository.findAll() >> Collections.emptyList()

        when:
        def result = reviewService.getReviews()

        then:
        result instanceof List<ReviewDto>
    }

    def "test updateReview"() {
        given:
        Long id = 1
        1 * reviewRepository.save(_) >> review
        1 * reviewRepository.findById(_) >> Optional.of(review)

        when:
        def response = reviewService.updateReview(1, reviewDto)

        then:
        response instanceof ReviewDto

    }


}

