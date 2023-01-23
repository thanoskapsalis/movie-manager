package com.intrasoft.moviemanager.service.interfaces;

import com.intrasoft.moviemanager.dto.ReviewDto;

import java.nio.file.NoSuchFileException;
import java.util.List;

public interface ReviewService {
    ReviewDto createReview(ReviewDto reviewDto);

    ReviewDto updateReview(long id, ReviewDto reviewDto) throws NoSuchFileException;

    ReviewDto getReview(long id) throws NoSuchFileException;

    List<ReviewDto> getReviews();

}
