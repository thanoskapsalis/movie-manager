package com.intrasoft.moviemanager.service.implementations;

import com.intrasoft.moviemanager.dto.MovieDto;
import com.intrasoft.moviemanager.dto.ReviewDto;
import com.intrasoft.moviemanager.entity.Movie;
import com.intrasoft.moviemanager.entity.Review;
import com.intrasoft.moviemanager.mapper.MovieMapper;
import com.intrasoft.moviemanager.mapper.ReviewMapper;
import com.intrasoft.moviemanager.repository.MovieRepository;
import com.intrasoft.moviemanager.repository.specificaton.MovieSpecification;
import com.intrasoft.moviemanager.service.interfaces.MovieService;
import com.intrasoft.moviemanager.service.interfaces.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final ReviewService reviewService;

    /**
     * Adds a new movie record in database
     *
     * @param movieDto
     * @return Object of type MovieDto
     */
    @Override
    public MovieDto createMovie(MovieDto movieDto) {
        Movie movieForSave = MovieMapper.INSTANCE.movieDtoToMovie(movieDto);

        // Update the movie with the current dateTime
        movieForSave.setTimeCreated(LocalDateTime.now());

        return MovieMapper.INSTANCE.movieToMovieDto(
                movieRepository.save(movieForSave)
        );
    }

    /**
     * Returns a movie from the database with the provided ID
     *
     * @param id
     * @return object of type MovieDto or null if movie with given id not exists
     */
    @Override
    public MovieDto getMovie(long id) throws NoSuchFileException {
        Optional<Movie> requestedMovie = movieRepository.findById(id);

        if (requestedMovie.isPresent())
            return MovieMapper.INSTANCE.movieToMovieDto(requestedMovie.get());

        throw new NoSuchFileException("Movie not found");
    }

    /**
     * Updates an already created movie
     *
     * @param movieDto
     * @return Object of type MovieDto
     */
    @Override
    public MovieDto updateMovie(long id, MovieDto movieDto) throws NoSuchFileException {

        // Get the movie by the id requested
        MovieDto currentMovie = getMovie(id);

        // Update info from the movieDto
        currentMovie.setMovieName(movieDto.getMovieName());
        currentMovie.setDescription(movieDto.getDescription());

        // Save the changes to the database
        return createMovie(currentMovie);
    }

    /**
     * Deletes a movie from the database with the requested id
     *
     * @param id
     */
    @Override
    public void deleteMovie(long id) {
        Optional<Movie> requestedMovie = movieRepository.findById(id);

        if (requestedMovie.isPresent())
            movieRepository.delete(requestedMovie.get());

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
     * Creates a new review on a movie found by id
     *
     * @param movieId
     * @param review
     * @return a ReviewDto Object
     */
    @Override
    @Transactional
    public ReviewDto reviewMovie(long id, ReviewDto reviewDto) throws NoSuchFileException {
        Movie requestedMovie = MovieMapper.INSTANCE.movieDtoToMovie(getMovie(id));

        // Save the new review before adding it on movie's review list
        Review review = ReviewMapper.INSTANCE.reviewDtoToReview(
                reviewService.createReview(reviewDto)
        );

        review.setMovie(requestedMovie);

        // Update the movie item and save changes
        requestedMovie.getMovieReviews().add(review);
        createMovie(MovieMapper.INSTANCE.movieToMovieDto(requestedMovie));

        return ReviewMapper.INSTANCE.reviewToReviewDto(review);
    }

    /**
     * Deletes a review of a specific movie
     * Movie and review are found by their id
     *
     * @param movieId
     * @param reviewId
     */
    public void deleteMovieReview(long movieId, long reviewId) throws NoSuchFileException {
        Movie requestedMovie = MovieMapper.INSTANCE.movieDtoToMovie(getMovie(movieId));
        requestedMovie
                .getMovieReviews()
                .removeIf(
                        review -> review.getId() == reviewId
                );

        createMovie(MovieMapper.INSTANCE.movieToMovieDto(requestedMovie));
    }

    /**
     * Updates the review of a movie item.
     * Before update, it checks if the review belongs to the movie.
     *
     * @param movieId
     * @param reviewId
     * @param reviewDto
     * @return
     */
    @Override
    public ReviewDto updateMovieReview(long movieId, long reviewId, ReviewDto reviewDto) throws NoSuchFileException {
        // First we get the movie item
        Movie movie = MovieMapper.INSTANCE.movieDtoToMovie(getMovie(movieId));

        // Then we get the review from its Id.
        // To be sure that the review has to do with the requested movie we load it from the review list
        Optional<Review> review = movie
                .getMovieReviews()
                .stream()
                .filter(rev -> rev.getId() == reviewId)
                .findFirst();

        // Return null if not found
        if (review.isEmpty())
            throw new NoSuchFileException("Review does not exist or not belongs to the movie");

        // Apply the updates
        return reviewService.updateReview(review.get().getId(), reviewDto);
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
