package com.zerobase.store_reservation.controller;

import com.zerobase.store_reservation.dto.ReviewInfo;
import com.zerobase.store_reservation.security.UserDetailsImpl;
import com.zerobase.store_reservation.service.ReviewService;
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

    // 리뷰 생성 API
    @PostMapping("/review")
    public ResponseEntity<String> createReview() {

    }

    // 리뷰 수정 API
    @PutMapping("/review")
    public ResponseEntity<String> updateReview() {

    }

    // 리뷰 삭제 API
    @DeleteMapping("/review")
    public ResponseEntity<String> deleteReview() {

    }
}
