package com.zerobase.store_reservation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignUpRequestDto {

    @NotBlank
    private String username;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    private boolean partner = false;
    private String partnerToken = "";
}
