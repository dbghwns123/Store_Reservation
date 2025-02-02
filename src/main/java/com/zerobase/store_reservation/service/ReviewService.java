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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;

    // 리뷰 조회 API
    public List<ReviewInfo> getReviews(User user) {
        List<Review> existReviews = reviewRepository.findAllByUser_id(user.getId());

        if (existReviews.isEmpty()) {
            throw new StoreException(ErrorCode.REVIEW_NOT_FOUND);
        }
        return existReviews.stream()
                .map(ReviewInfo::fromEntity)
                .collect(Collectors.toList());
    }

    // 리뷰 생성 API (예약 조회를 할 때 제공해주는 reservationId를 request 로 받아서 확인)
    public void createReview(@Valid CreateReview createReview, User user) {
        // reservationId와 해당 유저의 id로 예약된 내역이 있는지부터 확인
        Reservation existReservation = reservationRepository.findByIdAndUser_Id(createReview.getReservationId(), user.getId())
                .orElseThrow(() -> new StoreException(ErrorCode.RESERVATION_NOT_FOUND));
        // 예약 상태가 방문완료 상태가 아니면 예외 발생(방문 완료 시에만 리뷰 작성 가능)
        if (!existReservation.getStatus().equals(ReservationStatus.VISITED)) {
            throw new StoreException(ErrorCode.VISITED_NOT_YET);
        }
        Store store = storeRepository.findById(existReservation.getStore().getId()).get();

        reviewRepository.save(new Review(user, store, createReview.getTitle(), createReview.getDetail(), createReview.getRating()));
    }

    // 리뷰 수정 API
    public void updateReview(@Valid UpdateReview updateReview, User user) {
        // 해당 user가 작성한 review가 있는지부터 확인
        Review existReview = reviewRepository.findByIdAndUser_Id(updateReview.getReviewId(), user.getId())
                .orElseThrow(() -> new StoreException(ErrorCode.REVIEW_NOT_FOUND));

        existReview.updateReview(updateReview.getNewTitle(), updateReview.getNewDetail(), updateReview.getNewRating());
        reviewRepository.save(existReview);
    }

    // 리뷰 삭제 API
    public void deleteReview(@NotNull @Min(1) Long reviewId, User user) {
        // 우선 request 로 들어온 reviewId 로 해당 리뷰가 있는지 확인(없으면 예외)
        Review existByReviewId = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new StoreException(ErrorCode.REVIEW_NOT_FOUND));

        // review 가 있다면 우선 해당 리뷰의 사용자가 맞는지 검증(사용자가 맞다면 if 문 빠져나가서 바로 삭제 처리, 아니라면 두 번쨰 if 문)
        if (!Objects.equals(existByReviewId.getUser().getId(), user.getId())) {
            // 해당 리뷰의 가게의 점주인지 검증
            Long id = storeRepository.findById(existByReviewId.getStore().getId()).get().getUser().getId();
            // 로그인을 한 유저가 해당 가게의 점주인지 확인(맞다면 if 문 빠져나감, 아니라면 예외 발생)
            if (!Objects.equals(user.getId(), id)) {
                throw new StoreException(ErrorCode.NO_PERMISSION);
            }
        }
        // if 문을 빠져나왔다면 (해당 리뷰를 작성한 유저 또는 해당 리뷰의 가게의 점주이므로 해당 리뷰 삭제 처리)
        reviewRepository.delete(existByReviewId);
    }
}
