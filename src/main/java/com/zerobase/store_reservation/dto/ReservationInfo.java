package com.zerobase.store_reservation.dto;

import com.zerobase.store_reservation.entity.Reservation;
import com.zerobase.store_reservation.entity.Store;
import com.zerobase.store_reservation.type.ReservationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationInfo {

    private Long reservationId;
    private Long storeId;
    private String storeName;
    private LocalDateTime reservationTime;
    private ReservationStatus status;

    public static ReservationInfo fromEntity(Reservation reservation) {
        return ReservationInfo.builder()
                .reservationId(reservation.getId())
                .storeId(reservation.getStore().getId())
                .storeName(reservation.getStore().getStoreName()) // Store의 이름만 포함
                .reservationTime(reservation.getReservationTime())
                .status(reservation.getStatus())
                .build();
    }
}
