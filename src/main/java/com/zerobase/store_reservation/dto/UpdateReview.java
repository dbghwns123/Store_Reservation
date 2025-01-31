package com.zerobase.store_reservation.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateReview {

    @NotNull
    @Min(1)
    private Long reviewId;

    @NotBlank
    @Size(max = 50)
    private String newTitle;

    @NotBlank
    @Size(max = 300)
    private String newDetail;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer newRating;



}
