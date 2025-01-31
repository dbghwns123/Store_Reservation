package com.zerobase.store_reservation.service;

import com.zerobase.store_reservation.dto.CreateReview;
import com.zerobase.store_reservation.dto.ReviewInfo;
import com.zerobase.store_reservation.dto.UpdateReview;
import com.zerobase.store_reservation.entity.Reservation;
import com.zerobase.store_reservation.entity.Review;
import com.zerobase.store_reservation.entity.Store;
import com.zerobase.store_reservation.entity.User;
import com.zerobase.store_reservation.exception.StoreException;
import com.zerobase.store_reservation.repository.ReservationRepository;
import com.zerobase.store_reservation.repository.ReviewRepository;
import com.zerobase.store_reservation.repository.StoreRepository;
import com.zerobase.store_reservation.type.ErrorCode;
import com.zerobase.store_reservation.type.ReservationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;

    public List<ReviewInfo> getReviews(User user) {
        List<Review> existReviews = reviewRepository.findAllByUser_id(user.getId());

        if (existReviews.isEmpty()) {
            throw new StoreException(ErrorCode.REVIEW_NOT_FOUND);
        }
        return existReviews.stream()
                .map(ReviewInfo::fromEntity)
                .collect(Collectors.toList());
    }

    public void createReview(@Valid CreateReview createReview, User user) {
        Reservation existReservation = reservationRepository.findByIdAndUser_Id(createReview.getReservationId(), user.getId())
                .orElseThrow(() -> new StoreException(ErrorCode.RESERVATION_NOT_FOUND));
        // 예약 상태가 방문완료 상태가 아니면 예외 발생
        if (!existReservation.getStatus().equals(ReservationStatus.VISITED)) {
            throw new StoreException(ErrorCode.VISITED_NOT_YET);
        }
        Store store = storeRepository.findById(existReservation.getStore().getId()).get();

        reviewRepository.save(new Review(user, store, createReview.getTitle(), createReview.getDetail(), createReview.getRating()));
    }

    public void updateReview(@Valid UpdateReview updateReview, User user) {
        Review existReview = reviewRepository.findByIdAndUser_Id(updateReview.getReviewId(), user.getId())
                .orElseThrow(() -> new StoreException(ErrorCode.REVIEW_NOT_FOUND));

        existReview.updateReview(updateReview.getNewTitle(), updateReview.getNewDetail(), updateReview.getNewRating());
        reviewRepository.save(existReview);
    }
}
