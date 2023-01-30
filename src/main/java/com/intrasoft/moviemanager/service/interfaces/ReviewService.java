package com.intrasoft.moviemanager.service.interfaces;

import com.intrasoft.moviemanager.dto.ReviewDto;
import com.intrasoft.moviemanager.entity.Review;
import com.intrasoft.moviemanager.exception.NotFoundException;

import java.nio.file.NoSuchFileException;
import java.util.List;

public interface ReviewService {
    ReviewDto updateReview(long id, ReviewDto reviewDto) throws NotFoundException;
    ReviewDto getReviewDto(long id) throws NotFoundException;
    List<ReviewDto> getReviews();
    ReviewDto updateMovieReview(long movieId, long reviewId, ReviewDto reviewDto) throws NotFoundException;
    void deleteMovieReview(long movieId, long reviewId) throws NotFoundException;
    ReviewDto reviewMovie(long id, ReviewDto reviewDto) throws NotFoundException;
}
