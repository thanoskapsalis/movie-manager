package com.intrasoft.moviemanager.controller

import com.intrasoft.moviemanager.service.implementations.MovieServiceImpl
import com.intrasoft.moviemanager.service.implementations.ReviewServiceImpl
import org.springframework.http.HttpStatus
import spock.lang.Specification

class ReviewControllerSpec extends Specification {

    private ReviewController controller;
    private ReviewServiceImpl reviewService;

    def setup() {
        reviewService = Mock();
        controller = new ReviewController(reviewService);
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


}