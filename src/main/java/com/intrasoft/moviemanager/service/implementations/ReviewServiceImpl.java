package com.intrasoft.moviemanager.service.implementations;

import com.intrasoft.moviemanager.dto.MovieDto;
import com.intrasoft.moviemanager.dto.ReviewDto;
import com.intrasoft.moviemanager.entity.Movie;
import com.intrasoft.moviemanager.entity.Review;
import com.intrasoft.moviemanager.exception.NotFoundException;
import com.intrasoft.moviemanager.mapper.ReviewMapper;
import com.intrasoft.moviemanager.repository.ReviewRepository;
import com.intrasoft.moviemanager.service.interfaces.MovieService;
import com.intrasoft.moviemanager.service.interfaces.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieService movieService;

    /**
     * Gets a reviewDto object from its id
     *
     * @param id
     * @return ReviewDto
     * @throws NotFoundException
     */
    @Override
    public ReviewDto getReviewDto(long id) throws NotFoundException {
        return ReviewMapper.INSTANCE.reviewToReviewDto(
                reviewRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Review Not Found"))
        );
    }

    public Review getReview(long id) throws NotFoundException {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review Not Found"));
    }

    public ReviewDto saveReview(ReviewDto reviewDto) {
        return ReviewMapper.INSTANCE.reviewToReviewDto(
                reviewRepository.save(ReviewMapper.INSTANCE.reviewDtoToReview(reviewDto)));
    }

    /**
     * Returns all the review elements from database
     *
     * @return a List of ReviewDto Objects
     */
    @Override
    public List<ReviewDto> getReviews() {
        return ReviewMapper.INSTANCE.reviewToReviewDto(reviewRepository.findAll());
    }

    /**
     * Updates a review Item and returns it
     *
     * @param id
     * @param ReviewDto
     * @return ReviewDto
     */
    @Override
    public ReviewDto updateReview(long id, ReviewDto reviewDto) throws NotFoundException {
        ReviewDto currentReview = getReviewDto(id);

        //Update the fields of the review
        currentReview.setReviewerName(reviewDto.getReviewerName());
        currentReview.setReviewTitle(reviewDto.getReviewTitle());
        currentReview.setReviewContent(reviewDto.getReviewContent());

        // Save the changes to the database
        return saveReview(currentReview);

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
    public ReviewDto updateMovieReview(long movieId, long reviewId, ReviewDto reviewDto) throws NotFoundException {

        // First we get the movie item
        MovieDto movie = movieService.getMovieDto(movieId);

        // Then we get the review from its Id.
        // To be sure that the review has to do with the requested movie we load it from the review list
        Optional<ReviewDto> review = movie
                .getMovieReviews()
                .stream()
                .filter(rev -> rev.getId() == reviewId)
                .findFirst();

        // Return null if not found
        if (review.isEmpty())
            throw new NotFoundException("Review does not exist or not belongs to the movie");

        // Apply the updates
        return updateReview(review.get().getId(), reviewDto);
    }

    /**
     * Deletes a review of a specific movie
     * Movie and review are found by their id
     *
     * @param movieId
     * @param reviewId
     */
    @Override
    public void deleteMovieReview(long movieId, long reviewId) throws NotFoundException {

        MovieDto requestedMovie = movieService.getMovieDto(movieId);
        requestedMovie
                .getMovieReviews()
                .removeIf(
                        review -> review.getId() == reviewId
                );

        movieService.saveMovieDto(requestedMovie);
    }

    /**
     * Creates a new review on a movie found by id
     *
     * @param movieId
     * @param review
     * @return a ReviewDto Object
     */
    @Override
    public ReviewDto reviewMovie(long id, ReviewDto reviewDto) throws NotFoundException {
        Movie requestedMovie = movieService.getMovie(id);

        // Save the new review before adding it on movie's review list
        reviewDto.setMovie(requestedMovie);
        ReviewDto review = saveReview(reviewDto);

        // Update the movie item and save changes
        requestedMovie.getMovieReviews().add(
                ReviewMapper.INSTANCE.reviewDtoToReview(reviewDto)
        );
        movieService.saveMovie(requestedMovie);

        return review;
    }


}
