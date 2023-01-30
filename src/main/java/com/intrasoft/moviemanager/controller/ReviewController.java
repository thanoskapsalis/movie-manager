package com.intrasoft.moviemanager.controller;

import com.intrasoft.moviemanager.dto.ReviewDto;
import com.intrasoft.moviemanager.exception.NotFoundException;
import com.intrasoft.moviemanager.service.interfaces.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviews() {

        return ResponseEntity.ok(reviewService.getReviews());
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewDto> getReview(
            @PathVariable("id") long id
    ) throws NotFoundException {
        return ResponseEntity.ok(reviewService.getReviewDto(id));
    }

    @PutMapping("/{movieId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> updateMovieReview(
            @PathVariable("movieId") long movieId,
            @PathVariable("reviewId") long reviewId,
            @Valid @RequestBody ReviewDto reviewDto
    ) throws NotFoundException {
        log.info("Update movie review with id: {}", reviewId);
        return ResponseEntity.ok(reviewService.updateMovieReview(movieId, reviewId, reviewDto));
    }

    @DeleteMapping("/{movieId}/reviews/{reviewId}")
    // DK the same here
    public ResponseEntity<?> deleteMovieReview(
            @PathVariable("movieId") long movieId,
            @PathVariable("reviewId") long reviewId
    ) throws NotFoundException {
        reviewService.deleteMovieReview(movieId, reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<ReviewDto> createMovieReview(
            @PathVariable("id") long id,
            @Valid @RequestBody(required = true) ReviewDto reviewDto
    ) throws NotFoundException {
        return ResponseEntity.ok(reviewService.reviewMovie(id, reviewDto));
    }


}
