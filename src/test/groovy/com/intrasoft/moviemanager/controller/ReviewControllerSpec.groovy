package com.intrasoft.moviemanager.controller

import com.intrasoft.moviemanager.dto.ReviewDto
import com.intrasoft.moviemanager.service.implementations.ReviewServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class ReviewControllerSpec extends Specification {

    private ReviewController controller;
    private ReviewServiceImpl reviewService;

    def setup() {
        reviewService = Mock()
        controller = new ReviewController(reviewService)
    }

    def "ensure created"() {
        expect: "bean creation is successful"
        controller != null
    }

    def "test getReviews" () {
        when:
        def response = controller.getReviews()

        then:
        response.getStatusCode() == HttpStatus.OK
    }

    def "test getReview"() {
        given:
        Long  id = 1

        when:
        def response = controller.getReview(id)

        then:
        response.getStatusCode() == HttpStatus.OK
    }

    def "test createMovieReview"() {
        given:
        def reviewDto = ReviewDto.builder().build()
        Long id = 1

        when:
        def response = controller.createMovieReview(id, reviewDto)

        then:
        response instanceof ResponseEntity<ReviewDto>

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

    def "test deleteMovieReview"() {
        given:
        Long id = 1

        when:
        def response = controller.deleteMovieReview(id, id)

        then:
        response.getStatusCode() == HttpStatus.NO_CONTENT
    }


}