package com.intrasoft.moviemanager.repository.specificaton;

import com.intrasoft.moviemanager.entity.Movie;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovieSpecification implements Specification<Movie> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private Map<String, Object> filters = new HashMap<>();
    public void passFilters(HashMap<String, Object> filters) {
        this.filters = filters;
    }


    /**
     * Loop through filter hashmap and create the appropriate predicates for the filters
     * @param root
     * @param query
     * @param criteriaBuilder
     * @return
     */
    @Override
    public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        // List with predicates that represents filters on the query
        List<Predicate> predicateList = new ArrayList<>();

        // Iterate hashMap to make the query params
        for (Map.Entry<String, Object> filter : filters.entrySet()) {

            // If its DateTime then return only entities that are greater than of it
            if (filter.getValue() instanceof LocalDateTime) {
                predicateList.add(criteriaBuilder.greaterThan(
                        root.get(filter.getKey()),
                        LocalDateTime.parse(filter.getValue().toString(), dateTimeFormatter)
                ));
            }

            // Add to predicate list
            predicateList.add(
                    criteriaBuilder.equal(root.get(filter.getKey()), filter.getValue())
            );
        }

        return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
    }
}
