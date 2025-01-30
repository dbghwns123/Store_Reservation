package com.zerobase.store_reservation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationDto {

//    @NotBlank  // NotBlank 는 String 형식에만 적용됨
    @NotNull
    @Min(1) // 1 이상의 값만 허용
    private Long storeId;

    @NotNull
    private LocalDateTime reservationTime;

}
