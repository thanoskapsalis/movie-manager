package com.intrasoft.moviemanager.repository

import com.intrasoft.moviemanager.entity.Movie
import com.intrasoft.moviemanager.repository.specificaton.MovieSpecification
import spock.lang.Specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class MovieSpecificationSpec extends Specification {

    Root<Movie> root
    CriteriaQuery<?> criteriaQuery
    CriteriaBuilder criteriaBuilder

    def setup(){
        root = Mock(Root<Movie>)
        criteriaQuery = Mock(CriteriaQuery<?>)
        criteriaBuilder = Mock(CriteriaBuilder)
    }


    def "test toPredicate"() {
        given:

        HashMap<String, Object> addFilters = new HashMap<>()
        addFilters.put("movieName", "test")
        addFilters.put("description", "test")
        addFilters.put("timeCreated",LocalDateTime.of(2023,01,23,1,1,1))

        when:
        MovieSpecification ms = new MovieSpecification()
        ms.passFilters(addFilters)
        def result = ms.toPredicate(root,criteriaQuery, criteriaBuilder);

        then:
        noExceptionThrown()

    }
}
