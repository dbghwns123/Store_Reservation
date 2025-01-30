package com.zerobase.store_reservation.controller;

import com.zerobase.store_reservation.dto.ReservationDto;
import com.zerobase.store_reservation.dto.ReservationInfo;
import com.zerobase.store_reservation.dto.UpdateReservation;
import com.zerobase.store_reservation.security.UserDetailsImpl;
import com.zerobase.store_reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReservationController {

    private final ReservationService reservationService;

    // 매장 예약 조회 API
    @GetMapping("/reservation")
    public ResponseEntity<List<ReservationInfo>> getReservationInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ReservationInfo> reservationInfo = reservationService.getReservationInfo(userDetails.getUser());
        return ResponseEntity.ok(reservationInfo);
    }

    // 매장 예약 API
    @PostMapping("/reservation")
    public ResponseEntity<String> createReservation(@RequestBody @Valid ReservationDto reservationDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // 회원가입이 된 사람은 User 든 Partner 든 모두 매장 예약을 진행할 수 있다.
        reservationService.createReservation(reservationDto, userDetails.getUser());
        return ResponseEntity.ok("매장 예약이 완료되었습니다.");
    }

    // 매장 예약 수정 API (예약 시간만 변경 가능)
    @PutMapping("/reservation")
    public ResponseEntity<String> updateReservation(@RequestBody @Valid UpdateReservation updateReservation, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reservationService.updateReservation(updateReservation, userDetails.getUser());
        return ResponseEntity.ok("예약 수정이 성공적으로 완료되었습니다.");
    }

    // 매장 예약 취소 API
    @DeleteMapping("/reservation")
    public ResponseEntity<String> deleteReservation(@RequestBody @Valid ReservationDto reservationDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reservationService.deleteReservation(reservationDto, userDetails.getUser());

        return ResponseEntity.ok("해당 예약이 성공적으로 취소되었습니다.");
    }
}
