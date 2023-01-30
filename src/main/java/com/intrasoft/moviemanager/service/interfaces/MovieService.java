package com.intrasoft.moviemanager.service.interfaces;

import com.intrasoft.moviemanager.dto.MovieDto;
import com.intrasoft.moviemanager.entity.Movie;
import com.intrasoft.moviemanager.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface MovieService {
    MovieDto createMovie(MovieDto movieDto);

    MovieDto getMovieDto(long id) throws NotFoundException;

    Movie getMovie(long id) throws NotFoundException;

    MovieDto updateMovie(long id, MovieDto movieDto) throws NotFoundException;

    void deleteMovie(long id);

    List<MovieDto> getMovies();

    List<MovieDto> searchMovies(String movieName, String description, LocalDateTime timeCreated);

    MovieDto saveMovieDto(MovieDto movieDto);

    Movie saveMovie(Movie movie);
}
