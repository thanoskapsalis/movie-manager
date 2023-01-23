package com.intrasoft.moviemanager.controller;

import com.intrasoft.moviemanager.dto.ReviewDto;
import com.intrasoft.moviemanager.service.interfaces.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.NoSuchFileException;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviews() {

        return ResponseEntity.ok(reviewService.getReviews());
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewDto> getReview(
            @PathVariable("id") long id
    ) throws NoSuchFileException {
        return ResponseEntity.ok(reviewService.getReview(id));
    }


}
