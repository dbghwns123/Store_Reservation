package com.zerobase.store_reservation.service;

import com.zerobase.store_reservation.dto.ReviewInfo;
import com.zerobase.store_reservation.entity.Review;
import com.zerobase.store_reservation.entity.User;
import com.zerobase.store_reservation.exception.StoreException;
import com.zerobase.store_reservation.repository.ReviewRepository;
import com.zerobase.store_reservation.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<ReviewInfo> getReviews(User user) {
        List<Review> existReviews = reviewRepository.findAllByUser_id(user.getId());

        if (existReviews.isEmpty()) {
            throw new StoreException(ErrorCode.REVIEW_NOT_FOUND);
        }
        return existReviews.stream()
                .map(ReviewInfo::fromEntity)
                .collect(Collectors.toList());
    }
}
