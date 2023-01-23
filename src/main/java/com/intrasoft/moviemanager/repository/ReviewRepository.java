package com.intrasoft.moviemanager.repository;

import com.intrasoft.moviemanager.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
