package com.intrasoft.moviemanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intrasoft.moviemanager.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {
    private long id;
    private String reviewerName;
    @NotBlank(message = "Review title is required")
    private String reviewTitle;
    @NotBlank(message = "Review content is required")
    private String reviewContent;
    @JsonIgnore
    private Movie movie;
}
