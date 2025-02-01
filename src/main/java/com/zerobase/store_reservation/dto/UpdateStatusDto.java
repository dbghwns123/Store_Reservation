package com.zerobase.store_reservation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStatusDto {

    @NotNull
    @Min(1)
    private Long reservationId;

    @NotNull
    private Boolean status;
}
