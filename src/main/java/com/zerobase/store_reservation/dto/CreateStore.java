package com.zerobase.store_reservation.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CreateStore {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {


        @NotBlank
        private String storeName;

        @NotBlank
        private String location;

        @Size(min = 0, max = 500)
        private String description;

    }

}
