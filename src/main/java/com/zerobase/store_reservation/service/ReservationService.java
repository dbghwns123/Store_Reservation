package com.zerobase.store_reservation.service;

import com.zerobase.store_reservation.dto.ReservationDto;
import com.zerobase.store_reservation.dto.ReservationInfo;
import com.zerobase.store_reservation.dto.UpdateReservation;
import com.zerobase.store_reservation.entity.Reservation;
import com.zerobase.store_reservation.entity.Store;
import com.zerobase.store_reservation.entity.User;
import com.zerobase.store_reservation.exception.StoreException;
import com.zerobase.store_reservation.repository.ReservationRepository;
import com.zerobase.store_reservation.repository.StoreRepository;
import com.zerobase.store_reservation.type.ErrorCode;
import com.zerobase.store_reservation.type.ReservationStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;

    // 매장 예약 조회 API
    public List<ReservationInfo> getReservationInfo(User user) {
        List<Reservation> reservations = reservationRepository.findAllByUser_Id(user.getId());
        if (reservations.isEmpty()) {
            throw new IllegalArgumentException("아직 예약하신 내역이 없습니다.");
        }
        return reservations.stream()
                .map(ReservationInfo::fromEntity)
                .collect(Collectors.toList());
    }

    // 매장 예약 API
    @Transactional
    public void createReservation(@Valid ReservationDto reservationDto, User user) {
        Store store = storeRepository.findById(reservationDto.getStoreId())
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));

        reservationRepository.save(new Reservation(user, store, reservationDto.getReservationTime(), ReservationStatus.RESERVATION_SUCCESS));
    }

    // 매장 예약 수정 API (예약 시간만 변경 가능)
    public void updateReservation(@Valid UpdateReservation updateReservation, User user) {
        Reservation existReservation = reservationRepository.findByUser_IdAndStore_IdAndReservationTime(
                        user.getId(), updateReservation.getStoreId(), updateReservation.getReservationTime())
                .orElseThrow(() -> new StoreException(ErrorCode.RESERVATION_NOT_FOUND));
        existReservation.setReservationTime(updateReservation.getNewReservationTime());
        reservationRepository.save(existReservation);
    }

    // 매장 예약 취소 API
    public void deleteReservation(@Valid ReservationDto reservationDto, User user) {
        List<Reservation> existReservation = reservationRepository.findAllByUser_IdAndStore_IdAndReservationTime(
                user.getId(), reservationDto.getStoreId(), reservationDto.getReservationTime());
        if (existReservation.isEmpty()) {
            throw new StoreException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        reservationRepository.deleteAll(existReservation);
    }

    // 방문 예약 확인
    @Transactional
    public void visitStore(@Min(1) Long reservationId, User user) {
        Reservation reservation = reservationRepository.findByIdAndUser_Id(reservationId, user.getId())
                .orElseThrow(() -> new StoreException(ErrorCode.RESERVATION_NOT_FOUND));

        // 현재 방문한 시간이 예약시간에서 10분을 뺀 것보다 이후라면(10분 전에 도착 X)
        if (LocalDateTime.now().isAfter(reservation.getReservationTime().minusMinutes(10))) {
            throw new StoreException(ErrorCode.RESERVATION_LATE);
        }

        reservation.updateStatus();
        reservationRepository.save(reservation);
    }
}
