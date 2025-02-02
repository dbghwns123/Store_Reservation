package com.zerobase.store_reservation.controller;

import com.zerobase.store_reservation.dto.SignUpRequestDto;
import com.zerobase.store_reservation.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    // 회원 가입 API
    @PostMapping("/user/signup")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        userService.signUp(signUpRequestDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}
