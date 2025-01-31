package com.zerobase.store_reservation.controller;

import com.zerobase.store_reservation.dto.CreateReview;
import com.zerobase.store_reservation.dto.ReviewInfo;
import com.zerobase.store_reservation.security.UserDetailsImpl;
import com.zerobase.store_reservation.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 조회 API
    @GetMapping("/review")
    public ResponseEntity<List<ReviewInfo>> getReviews(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ReviewInfo> reviews = reviewService.getReviews(userDetails.getUser());
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 생성 API (예약 조회를 할 때 제공해주는 reservationId를 request 로 받아서 확인)
    @PostMapping("/review")
    public ResponseEntity<String> createReview(@RequestBody @Valid CreateReview createReview, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.createReview(createReview, userDetails.getUser());
        return ResponseEntity.ok("리뷰 작성이 성공적으로 완료되었습니다.");
    }

    // 리뷰 수정 API
    @PutMapping("/review")
    public ResponseEntity<String> updateReview() {
        return ResponseEntity.ok("");
    }

    // 리뷰 삭제 API
    @DeleteMapping("/review")
    public ResponseEntity<String> deleteReview() {
        return ResponseEntity.ok("");
    }
}
