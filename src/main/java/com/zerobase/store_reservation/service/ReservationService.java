package com.zerobase.store_reservation.service;

import com.zerobase.store_reservation.dto.DeleteReservation;
import com.zerobase.store_reservation.dto.ReservationDto;
import com.zerobase.store_reservation.dto.ReservationInfo;
import com.zerobase.store_reservation.entity.Reservation;
import com.zerobase.store_reservation.entity.Store;
import com.zerobase.store_reservation.entity.User;
import com.zerobase.store_reservation.exception.StoreException;
import com.zerobase.store_reservation.repository.ReservationRepository;
import com.zerobase.store_reservation.repository.StoreRepository;
import com.zerobase.store_reservation.type.ErrorCode;
import com.zerobase.store_reservation.type.ReservationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    // 매장 예약 취소 API
    public void deleteReservation(@Valid DeleteReservation deleteReservation, User user) {
        List<Reservation> existReservation = reservationRepository.findAllByUser_IdAndStore_IdAndReservationTime(
                user.getId(), deleteReservation.getStoreId(), deleteReservation.getReservationTime());
        if (existReservation.isEmpty()) {
            throw new StoreException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        reservationRepository.deleteAll(existReservation);
    }
}
