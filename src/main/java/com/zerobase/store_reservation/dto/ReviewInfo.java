package com.zerobase.store_reservation.dto;

import com.zerobase.store_reservation.entity.Review;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewInfo {

    private Long id;
    private String title;
    private String detail;
    private Integer rating;

    public static ReviewInfo fromEntity(Review review) {
        return ReviewInfo.builder()
                .id(review.getId())
                .title(review.getTitle())
                .detail(review.getDetail())
                .rating(review.getRating())
                .build();
    }
}
