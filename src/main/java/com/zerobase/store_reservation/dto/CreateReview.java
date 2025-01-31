package com.zerobase.store_reservation.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReview {

    @NotNull
    @Min(1)
    private Long reservationId;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 300)
    private String detail;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}
