package com.zerobase.store_reservation.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    STORE_ALREADY_REGISTRATION("이미 등록된 상호명입니다."),
    STORE_NOT_FOUND("해당 매장이 등록되어 있지 않습니다."),
    RESERVATION_NOT_FOUND("해당 예약이 등록되어 있지 않습니다."),
    RESERVATION_LATE("예약시간 10분 전까지만 방문 확인이 가능합니다."),
    REVIEW_NOT_FOUND("작성하신 리뷰가 없습니다."),
    VISITED_NOT_YET("매장 이용 후에 리뷰를 작성할 수 있습니다.")
    ;


    private final String description;

}
