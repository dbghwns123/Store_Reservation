package com.zerobase.store_reservation.repository;

import com.zerobase.store_reservation.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByUser_id(Long userId);
}
