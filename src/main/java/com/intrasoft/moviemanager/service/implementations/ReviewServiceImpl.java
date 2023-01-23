package com.intrasoft.moviemanager.service.implementations;

import com.intrasoft.moviemanager.dto.ReviewDto;
import com.intrasoft.moviemanager.entity.Review;
import com.intrasoft.moviemanager.mapper.ReviewMapper;
import com.intrasoft.moviemanager.repository.ReviewRepository;
import com.intrasoft.moviemanager.service.interfaces.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * Adds a new review at the database
     *
     * @param reviewDto
     * @return Object of type ReviewDto
     */
    @Override
    public ReviewDto createReview(ReviewDto reviewDto) {
        Review review = ReviewMapper.INSTANCE.reviewDtoToReview(reviewDto);
        return ReviewMapper.INSTANCE.reviewToReviewDto(
                reviewRepository.save(review)
        );
    }

    @Override
    public ReviewDto getReview(long id) throws NoSuchFileException {
        Optional<Review> review = reviewRepository.findById(id);

        if (review.isPresent())
            return ReviewMapper.INSTANCE.reviewToReviewDto(
                    review.get()
            );

        throw new NoSuchFileException("Review with requested id not found");

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
     * @param reviewDto
     * @return
     */
    @Override
    public ReviewDto updateReview(long id, ReviewDto reviewDto) throws NoSuchFileException {
        ReviewDto currentReview = getReview(id);

        //Update the fields of the review
        currentReview.setReviewerName(reviewDto.getReviewerName());
        currentReview.setReviewTitle(reviewDto.getReviewTitle());
        currentReview.setReviewContent(reviewDto.getReviewContent());

        // Save the changes to the database
        return createReview(currentReview);

    }


}
