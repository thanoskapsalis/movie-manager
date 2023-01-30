package com.intrasoft.moviemanager.controller;

import com.intrasoft.moviemanager.dto.MovieDto;
import com.intrasoft.moviemanager.exception.NotFoundException;
import com.intrasoft.moviemanager.service.interfaces.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    ) throws NotFoundException {
        return ResponseEntity.ok(movieService.getMovieDto(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieDto>> searchMovie(
            @RequestParam(required = false) String movieName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime timeCreated
    ) {
        List<MovieDto> movieDtos = movieService.searchMovies(movieName, description, timeCreated);
        return movieDtos.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.ok(movieDtos);
    }

    @PostMapping
    public ResponseEntity<MovieDto> createNewMovie(
            @Valid @RequestBody MovieDto movieDto
    ) {
        log.info("New Movie with name: {}", movieDto.getMovieName());
        return ResponseEntity.ok(movieService.createMovie(movieDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(
            @PathVariable("id") long id,
            @Valid @RequestBody(required = true) MovieDto movieDto
    ) throws NotFoundException {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(
            @PathVariable("id") long id
    ) {
        movieService.deleteMovie(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
