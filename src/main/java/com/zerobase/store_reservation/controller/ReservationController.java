package com.zerobase.store_reservation.controller;

import com.zerobase.store_reservation.dto.ReservationDto;
import com.zerobase.store_reservation.dto.ReservationInfo;
import com.zerobase.store_reservation.dto.UpdateReservation;
import com.zerobase.store_reservation.security.UserDetailsImpl;
import com.zerobase.store_reservation.service.ReservationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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

    // 사용자가 매장에 방문하여 키오스크를 통해 방문을 확인하는 API (예약 상태를 예약성공 -> 방문 으로 바꿔줌)
    // RequestParam 으로 받는 reservationId 는 사용자가 예약 조회를 할 때 제공해주는 데이터로 활용 (번호표 같은 개념)
    @PutMapping("/kiosk")
    public ResponseEntity<String> visitStore(@RequestParam @Min(1) Long reservationId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reservationService.visitStore(reservationId, userDetails.getUser());
        return ResponseEntity.ok("방문 확인이 완료되었습니다. 잠시 후에 입장 가능합니다.");
    }
}
