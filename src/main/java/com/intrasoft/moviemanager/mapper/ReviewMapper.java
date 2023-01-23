package com.intrasoft.moviemanager.mapper;

import com.intrasoft.moviemanager.dto.ReviewDto;
import com.intrasoft.moviemanager.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    ReviewDto reviewToReviewDto(Review review);

    Review reviewDtoToReview(ReviewDto reviewDto);

    List<Review> reviewDtoToReview(List<ReviewDto> reviewDtoList);

    List<ReviewDto> reviewToReviewDto(List<Review> reviewList);
}
