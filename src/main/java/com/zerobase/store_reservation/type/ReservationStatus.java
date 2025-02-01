package com.zerobase.store_reservation.type;

import lombok.Getter;

@Getter
public enum ReservationStatus {

    RESERVATION_WAITING,
    RESERVATION_FAIL,
    RESERVATION_SUCCESS,
    VISITED,
    ;

}
