package com.zerobase.store_reservation.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateReservation {

    @NotNull
    @Min(1)
    private Long storeId;

    @NotNull
    private LocalDateTime reservationTime;

    @NotNull
    private LocalDateTime newReservationTime;


}
