package com.zerobase.store_reservation.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    STORE_ALREADY_REGISTRATION("이미 등록된 상호명입니다."),
    STORE_NOT_FOUND("해당 매장이 등록되어 있지 않습니다."),

    ;


    private final String description;

}
