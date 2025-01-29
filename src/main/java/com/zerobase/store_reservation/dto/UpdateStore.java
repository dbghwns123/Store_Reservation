package com.zerobase.store_reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class UpdateStore {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {

        private Long storeId;
        private String StoreName;
        private String Location;
        private String Description;

    }
}
