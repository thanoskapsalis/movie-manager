package com.intrasoft.moviemanager.mapper

import com.intrasoft.moviemanager.dto.MovieDto
import com.intrasoft.moviemanager.dto.ReviewDto
import com.intrasoft.moviemanager.entity.Movie
import com.intrasoft.moviemanager.entity.Review
import org.codehaus.groovy.runtime.wrappers.PojoWrapper
import spock.lang.Specification

import java.time.LocalDateTime

class ReviewMapperSpec extends Specification {

    ReviewDto reviewDto;
    Review review;

    def setup() {
        reviewDto = ReviewDto.builder()
                .id(1)
                .reviewerName("reviewName")
                .reviewContent("reviewContent")
                .reviewTitle("reviewTitle")
                .build()

        review = Review.builder()
                .reviewTitle("reviewTitle")
                .reviewContent("reviewContent")
                .reviewerName("reviewName")
                .id(1)
                .build()
    }

    def "test reviewToReviewDto Object"() {
        when:
        def result = ReviewMapper.INSTANCE.reviewToReviewDto(review)

        then:
        result instanceof ReviewDto
    }


    def "test reviewToReviewDto List"() {
        given:
        Review review2 = Review.builder()
                .reviewTitle("reviewTitle")
                .reviewContent("reviewContent")
                .reviewerName("reviewName")
                .id(1)
                .build()

        List<Review> reviews = new ArrayList<>()
        reviews.add(review)
        reviews.add(review2)

        when:
        def result =  ReviewMapper.INSTANCE.reviewToReviewDto(reviews)

        then:
        result instanceof List<ReviewDto>
    }

    def "test reviewDtoToReview Object"() {
        when:
        def result = ReviewMapper.INSTANCE.reviewDtoToReview(reviewDto)

        then:
        result instanceof Review
    }

    def "test reviewDtoToReview List"() {
        given:
        ReviewDto reviewDto2 = ReviewDto.builder()
                .reviewTitle("reviewTitle")
                .reviewContent("reviewContent")
                .reviewerName("reviewName")
                .id(1)
                .build()

        List<ReviewDto> reviewDtos = new ArrayList<>()
        reviewDtos.add(reviewDto)
        reviewDtos.add(reviewDto2)

        when:
        def result =  ReviewMapper.INSTANCE.reviewDtoToReview(reviewDtos)

        then:
        result instanceof List<Review>
    }

    def "test reviewToReviewDto return null"() {
        when:
        def input = new PojoWrapper(null, Review)
        def result = ReviewMapper.INSTANCE.reviewToReviewDto(null)
        MovieDto reviewDtoNull = ReviewMapper.INSTANCE.reviewToReviewDto(input)

        then:
        result == null
        reviewDtoNull == null
    }

    def "test reviewDtoToReview return null"() {
        when:
        def input = new PojoWrapper(null, ReviewDto)
        def result = ReviewMapper.INSTANCE.reviewDtoToReview(null)
        Review reviewNull = ReviewMapper.INSTANCE.reviewDtoToReview(input)

        then:
        result == null
        reviewNull == null
    }

}

