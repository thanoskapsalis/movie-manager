package com.intrasoft.moviemanager.service.interfaces;

import com.intrasoft.moviemanager.dto.MovieDto;
import com.intrasoft.moviemanager.dto.ReviewDto;

import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.util.List;

public interface MovieService {
    MovieDto createMovie(MovieDto movieDto);
    MovieDto getMovie(long id) throws NoSuchFileException;
    MovieDto updateMovie(long id, MovieDto movieDto) throws NoSuchFileException;
    void deleteMovie(long id);
    List<MovieDto> getMovies();
    ReviewDto reviewMovie(long movieId, ReviewDto review) throws NoSuchFileException;
    void deleteMovieReview(long movieId, long reviewId) throws NoSuchFileException;
    ReviewDto updateMovieReview(long movieId, long reviewId, ReviewDto reviewDto) throws NoSuchFileException;
    List<MovieDto> searchMovies(String movieName, String description, LocalDateTime timeCreated);
}
