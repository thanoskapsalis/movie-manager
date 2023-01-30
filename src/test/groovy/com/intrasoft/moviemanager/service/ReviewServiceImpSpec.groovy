package com.intrasoft.moviemanager.service

import com.intrasoft.moviemanager.dto.MovieDto
import com.intrasoft.moviemanager.dto.ReviewDto
import com.intrasoft.moviemanager.entity.Movie
import com.intrasoft.moviemanager.entity.Review
import com.intrasoft.moviemanager.exception.NotFoundException
import com.intrasoft.moviemanager.mapper.MovieMapper
import com.intrasoft.moviemanager.mapper.ReviewMapper
import com.intrasoft.moviemanager.repository.MovieRepository
import com.intrasoft.moviemanager.repository.ReviewRepository
import com.intrasoft.moviemanager.service.implementations.MovieServiceImpl
import com.intrasoft.moviemanager.service.implementations.ReviewServiceImpl
import spock.lang.Specification

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
        movieService = Mock()
        reviewService = new ReviewServiceImpl(reviewRepository, movieService)

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
                .movieReviews(new ArrayList<ReviewDto>())
                .build()

        review = Review.builder()
                .reviewTitle("reviewTitle")
                .reviewContent("reviewContent")
                .reviewerName("reviewName")
                .id(1)
                .build()
    }

    def "test saveReview"() {
        given:
        1 * reviewRepository.save(_) >> review

        when:
        def result = reviewService.saveReview(reviewDto)

        then:
        result instanceof ReviewDto
    }

    def "test getReviewDto"() {
        given:
        Long id = 1
        1 * reviewRepository.findById(_) >> Optional.of(review)

        when:
        def result = reviewService.getReviewDto(1)

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
        result instanceof Review
    }

    def "test getReview throws NotFoundException"() {
        given:
        Long id = 1
        1 * reviewRepository.findById(_) >> Optional.empty()

        when:
        def result = reviewService.getReview(1)

        then:
        thrown(NotFoundException)
    }

    def "test getReviewDto throws NotFoundException"() {
        given:
        Long id = 1
        1 * reviewRepository.findById(_) >> Optional.empty()

        when:
        def result = reviewService.getReviewDto(1)

        then:
        thrown(NotFoundException)
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

    def "test reviewMovie"() {
        given:

        ReviewDto reviewDtoWithMovie = ReviewDto.builder()
                .id(1)
                .reviewerName("reviewName")
                .reviewContent("reviewContent")
                .reviewTitle("reviewTitle")
                .movie(movie)
                .build()

        Long id = 1;
        1 * reviewRepository.save(_) >> review
        1 * movieService.getMovieDto(_) >> movieDto
        1 * movieService.saveMovieDto(_) >> movieDto

        when:
        def result = reviewService.reviewMovie(id, reviewDtoWithMovie)

        then:
        result instanceof ReviewDto
    }

    def "test deleteMovieReview with no reviews"() {
        given:
        Long movieId = 1
        Long reviewId = 1

        1 * movieService.getMovieDto(_) >> movieDto

        when:
        reviewService.deleteMovieReview(movieId, reviewId)

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

        1 * movieService.getMovieDto(_) >> movieDto

        when:
        movieWithReview.getMovieReviews().add(review)
        reviewService.deleteMovieReview(movieId, reviewId)

        then:
        noExceptionThrown()
    }


    def "test updateMovieReview"() {
        given:
        Long movieId = 1
        Long reviewId = 1

        Review reviewWithMovie = Review.builder()
                .reviewTitle("reviewTitle")
                .reviewContent("reviewContent")
                .reviewerName("reviewName")
                .id(1)
                .build()

        Movie movie2 = Movie.builder()
                .movieName("Sample name")
                .id(1)
                .description("test")
                .timeCreated(LocalDateTime.now())
                .movieReviews(new ArrayList<Review>())
                .build()

        ReviewDto reviewWithMovieDto = ReviewMapper.INSTANCE.reviewToReviewDto(reviewWithMovie)
        movie2.getMovieReviews().add(reviewWithMovie)

        1 * movieService.getMovieDto(1) >> MovieMapper.INSTANCE.movieToMovieDto(movie2)
        1 * reviewRepository.findById(_) >> Optional.of(review)
        1 * reviewRepository.save(_) >> review

        when:
        def result = reviewService.updateMovieReview(movieId, reviewId, reviewWithMovieDto)

        then:
        result instanceof ReviewDto
    }

    def "test updateMovieReview throws NotFoundException"() {
        given:
        Long movieId = 1
        Long reviewId = 1

        Review reviewWithMovie = Review.builder()
                .reviewTitle("reviewTitle")
                .reviewContent("reviewContent")
                .reviewerName("reviewName")
                .id(1)
                .build()

        Movie movie2 = Movie.builder()
                .movieName("Sample name")
                .id(1)
                .description("test")
                .timeCreated(LocalDateTime.now())
                .movieReviews(new ArrayList<Review>())
                .build()

        ReviewDto reviewWithMovieDto = ReviewMapper.INSTANCE.reviewToReviewDto(reviewWithMovie)

        1 * movieService.getMovieDto(1) >> MovieMapper.INSTANCE.movieToMovieDto(movie2)

        when:
        reviewService.updateMovieReview(movieId, reviewId, reviewWithMovieDto)

        then:
        thrown(NotFoundException)
    }

}

