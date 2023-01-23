package com.intrasoft.moviemanager.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MovieDto {
    private Long id;
    @NotBlank(message = "Movie Name is mandatory")
    private String movieName;
    @NotBlank(message = "Description is mandatory")
    private String description;
    private LocalDateTime timeCreated;
    private List<ReviewDto> movieReviews;

}
