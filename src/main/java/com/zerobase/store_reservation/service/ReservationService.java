package com.zerobase.store_reservation.service;

import com.zerobase.store_reservation.dto.ReservationDto;
import com.zerobase.store_reservation.dto.ReservationInfo;
import com.zerobase.store_reservation.dto.UpdateReservation;
import com.zerobase.store_reservation.dto.UpdateStatusDto;
import com.zerobase.store_reservation.entity.Reservation;
import com.zerobase.store_reservation.entity.Store;
import com.zerobase.store_reservation.entity.User;
import com.zerobase.store_reservation.exception.StoreException;
import com.zerobase.store_reservation.repository.ReservationRepository;
import com.zerobase.store_reservation.repository.StoreRepository;
import com.zerobase.store_reservation.repository.UserRepository;
import com.zerobase.store_reservation.type.ErrorCode;
import com.zerobase.store_reservation.type.ReservationStatus;
import com.zerobase.store_reservation.type.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

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

    public List<ReservationInfo> getReservations(Long storeId, User user) {
        // user 가 partner 인지 일반 user 인지 확인 (일반 유저라면 예외 발생)
        isPartner(user);
        // StoreService -> getPartnerStores 에서 제공되는 가게 id로 예약조회를 하게 될 것이므로 검증없이 바로 사용 가능
        List<Reservation> reservations = reservationRepository.findAllByStore_Id(storeId);

        return reservations.stream()
                .map(ReservationInfo::fromEntity)
                .collect(Collectors.toList());
    }

    // 매장 예약 API
    @Transactional
    public void createReservation(@Valid ReservationDto reservationDto, User user) {
        Store store = storeRepository.findById(reservationDto.getStoreId())
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));

        reservationRepository.save(new Reservation(user, store, reservationDto.getReservationTime(), ReservationStatus.RESERVATION_WAITING));
    }

    // 매장 예약 수정 API (예약 시간만 변경 가능)
    public void updateReservation(@Valid UpdateReservation updateReservation, User user) {
        Reservation existReservation = reservationRepository.findByUser_IdAndStore_IdAndReservationTime(
                        user.getId(), updateReservation.getStoreId(), updateReservation.getReservationTime())
                .orElseThrow(() -> new StoreException(ErrorCode.RESERVATION_NOT_FOUND));
        existReservation.setReservationTime(updateReservation.getNewReservationTime());
        reservationRepository.save(existReservation);
    }

    public void updateStatus(@Valid UpdateStatusDto updateStatus, User user) {

        // user 가 partner 인지 일반 user 인지 확인 (일반 유저라면 예외 발생)
        isPartner(user);

        // 바꿀 예약의 상태가 waiting 이 맞는지 검증
        Reservation reservation = reservationRepository.findById(updateStatus.getReservationId()).get();
        if (!reservation.getStatus().equals(ReservationStatus.RESERVATION_WAITING)) {
            throw new StoreException(ErrorCode.NOT_WAITING_STATUS);
        }

        // 상태 변경을 하려는 partner 가 매장 주인이 맞는지 검증
        isOwner(user, reservation);

        if (updateStatus.getStatus()) {
            reservation.updateStatus(ReservationStatus.RESERVATION_SUCCESS);
        } else {
            reservation.updateStatus(ReservationStatus.RESERVATION_FAIL);
        }
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

        reservation.updateStatus(ReservationStatus.VISITED);
        reservationRepository.save(reservation);
    }

    // user 가 partner 가 맞는지 확인하는 메서드
    private void isPartner(User user) {
        User existUser = userRepository.findById(user.getId()).get();
        if (existUser.getUserRole().equals(UserRole.USER)) {
            throw new StoreException(ErrorCode.NO_PERMISSION);
        }
    }

    // user 가 매장 주인이 맞는지 검증하는 메서드
    private void isOwner(User user, Reservation reservation) {
        Store store = storeRepository.findById(reservation.getStore().getId()).get();
        if (!Objects.equals(store.getUser().getId(), user.getId())) {
            throw new StoreException(ErrorCode.NO_PERMISSION);
        }
    }


}
