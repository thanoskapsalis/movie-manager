package com.intrasoft.moviemanager.service.implementations;

import com.intrasoft.moviemanager.dto.MovieDto;
import com.intrasoft.moviemanager.entity.Movie;
import com.intrasoft.moviemanager.exception.NotFoundException;
import com.intrasoft.moviemanager.mapper.MovieMapper;
import com.intrasoft.moviemanager.repository.MovieRepository;
import com.intrasoft.moviemanager.repository.specificaton.MovieSpecification;
import com.intrasoft.moviemanager.service.interfaces.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    /**
     * Adds a new movie record in database
     *
     * @param movieDto
     * @return Object of type MovieDto
     */
    @Override
    public MovieDto createMovie(MovieDto movieDto) {

        // Update the movie with the current dateTime
        movieDto.setTimeCreated(LocalDateTime.now());

        return saveMovieDto(movieDto);
    }

    /**
     * Returns a movie from the database with the provided ID as Dto object
     *
     * @param id
     * @return MovieDto
     */
    @Override
    public MovieDto getMovieDto(long id) throws NotFoundException {
        return MovieMapper.INSTANCE.movieToMovieDto(movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found")));
    }

    @Override
    public Movie getMovie(long id) throws NotFoundException {
        return movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie Not Found"));
    }

    @Override
    public MovieDto saveMovieDto(MovieDto movieDto) {
        Movie tosave = MovieMapper.INSTANCE.movieDtoToMovie(movieDto);
        return MovieMapper.INSTANCE.movieToMovieDto(movieRepository.save(tosave));
    }

    @Override
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    /**
     * Updates an already created movie
     *
     * @param movieDto
     * @return MovieDto
     */
    @Override
    public MovieDto updateMovie(long id, MovieDto movieDto) throws NotFoundException {

        // Get the movie by the id requested
        MovieDto currentMovie = getMovieDto(id);

        // Update info from the movieDto
        currentMovie.setMovieName(movieDto.getMovieName());
        currentMovie.setDescription(movieDto.getDescription());

        // Save the changes to the database
        return saveMovieDto(currentMovie);
    }

    /**
     * Deletes a movie from the database with the requested id
     *
     * @param id
     */
    @Override
    public void deleteMovie(long id) {
        movieRepository.deleteById(id);
    }

    /**
     * Returns a list with with all the movies found in the database
     *
     * @return A List of MovieDto Objects
     */
    @Override
    public List<MovieDto> getMovies() {
        return MovieMapper.INSTANCE.movieToMovieDto(
                movieRepository.findAll()
        );
    }

    /**
     * Returns a list of MovieDto objects according to searchCriteria
     *
     * @param movieName, description , timeCreated
     * @return
     */
    @Override
    public List<MovieDto> searchMovies(String movieName, String description, LocalDateTime timeCreated) {
        HashMap<String, Object> params = new HashMap<>();

        if (movieName != null) {
            params.put("movieName", movieName);
        }

        if (description != null) {
            params.put("description", description);
        }

        if (timeCreated != null) {
            params.put("timeCreated", timeCreated);
        }

        // Filter the results through movieSpectification
        MovieSpecification movieSpecification = new MovieSpecification();
        movieSpecification.passFilters(params);

        return MovieMapper.INSTANCE.movieToMovieDto(movieRepository.findAll(movieSpecification));
    }
}
