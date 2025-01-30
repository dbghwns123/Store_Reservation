package com.zerobase.store_reservation.service;

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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;


    public List<ReservationInfo> getReservationInfo(User user) {
        List<Reservation> reservations = reservationRepository.findAllByUser(user);
        if (reservations.isEmpty()) {
            throw new IllegalArgumentException("아직 예약하신 내역이 없습니다.");
        }
        return reservations.stream()
                .map(ReservationInfo::fromEntity)
                .collect(Collectors.toList());
    }

    public void createReservation(@Valid ReservationDto reservationDto, User user) {
        Store store = storeRepository.findById(reservationDto.getStoreId())
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));

        reservationRepository.save(new Reservation(user, store, reservationDto.getReservationTime(), ReservationStatus.RESERVATION_SUCCESS));
    }


}
