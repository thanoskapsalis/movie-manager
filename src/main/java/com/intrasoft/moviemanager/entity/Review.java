package com.intrasoft.moviemanager.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "REVIEWS")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String reviewerName;
    private String reviewTitle;
    private String reviewContent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;
}