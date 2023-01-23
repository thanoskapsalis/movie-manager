package com.intrasoft.moviemanager.controller;

import com.intrasoft.moviemanager.dto.MovieDto;
import com.intrasoft.moviemanager.dto.ReviewDto;
import com.intrasoft.moviemanager.service.interfaces.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@Slf4j
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getMovies() {
        return ResponseEntity.ok(movieService.getMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovie(
            @PathVariable("id") Long id
    ) throws IOException {
        log.info("Return Movie with id: {}", id);
        return ResponseEntity.ok(movieService.getMovie(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieDto>> searchMovie(
            @RequestParam(required = false) String movieName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false)
                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime timeCreated
    ) {
        log.info("Searching for movies with filters");
        List<MovieDto> movieDtos = movieService.searchMovies(movieName, description, timeCreated);
        return movieDtos.isEmpty() ?  ResponseEntity.status(HttpStatus.NO_CONTENT).build() :ResponseEntity.ok(movieDtos) ;
    }

    @PostMapping
    public ResponseEntity<MovieDto> createNewMovie(
            @Valid @RequestBody MovieDto movieDto
    ) {
        log.info("New Movie with name: {}", movieDto.getMovieName());
        return ResponseEntity.ok(movieService.createMovie(movieDto));
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<ReviewDto> createMovieReview(
            @PathVariable("id") long id,
            @Valid @RequestBody(required = true) ReviewDto reviewDto
    ) throws IOException {
        log.info("New review on movie {} Reviewer", id, reviewDto.getReviewerName());
        return ResponseEntity.ok(movieService.reviewMovie(id, reviewDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(
            @PathVariable("id") long id,
            @Valid @RequestBody(required = true) MovieDto movieDto
    ) throws IOException {
        log.info("Update Movie with id: {}", id);
        return ResponseEntity.ok(movieService.updateMovie(id, movieDto));
    }

    @PutMapping("/{movieId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> updateMovieReview(
            @PathVariable("movieId") long movieId,
            @PathVariable("reviewId") long reviewId,
            @Valid @RequestBody ReviewDto reviewDto
    ) throws IOException {
        log.info("Update movie review with id: {}", reviewId);
        return ResponseEntity.ok(movieService.updateMovieReview(movieId, reviewId, reviewDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMovie(
            @PathVariable("id") long id
    ) {
        log.info("Movie with id {} deleted", id);
        movieService.deleteMovie(id);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{movieId}/reviews/{reviewId}")
    public ResponseEntity deleteMovieReview(
            @PathVariable("movieId") long movieId,
            @PathVariable("reviewId") long reviewId
    ) throws IOException {
        log.info("Review with id {} deleted", reviewId);
        movieService.deleteMovieReview(movieId, reviewId);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

}
