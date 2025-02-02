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
        // 로그인한 user 의 id 로 예약한 내역을 모두 찾아옴 (없을 시 예외)
        List<Reservation> reservations = reservationRepository.findAllByUser_Id(user.getId());
        if (reservations.isEmpty()) {
            throw new StoreException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        return reservations.stream()
                .map(ReservationInfo::fromEntity)
                .collect(Collectors.toList());
    }

    // 매장 예약 조회 API (점주)
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

        // 점주가 확인을 하고 예약 승인/거절 여부를 정해야 하므로 처음에는 waiting 로 상태를 설정
        reservationRepository.save(new Reservation(user, store, reservationDto.getReservationTime(), ReservationStatus.RESERVATION_WAITING));
    }

    // 매장 예약 수정 API (예약 시간만 변경 가능)
    public void updateReservation(@Valid UpdateReservation updateReservation, User user) {
        // request 로 storeId, 예약시간과 user 가 넘어오므로 해당 예약이 있는지 확인하고 없으면 예외 발생
        Reservation existReservation = reservationRepository.findByUser_IdAndStore_IdAndReservationTime(
                        user.getId(), updateReservation.getStoreId(), updateReservation.getReservationTime())
                .orElseThrow(() -> new StoreException(ErrorCode.RESERVATION_NOT_FOUND));
        existReservation.setReservationTime(updateReservation.getNewReservationTime());
        reservationRepository.save(existReservation);
    }

    // 점주가 매장 예약을 조회하고 그 중 status 가 waiting 상태인 예약 id와 바꿀 상태를 넘겨주면 상태를 업데이트 하는 API
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

        // 넘어온 status 값이 true 라면 예약 승인, false 라면 예약 실패 status 업데이트
        if (updateStatus.getStatus()) {
            reservation.updateStatus(ReservationStatus.RESERVATION_SUCCESS);
        } else {
            reservation.updateStatus(ReservationStatus.RESERVATION_FAIL);
        }
    }

    // 매장 예약 취소 API
    public void deleteReservation(@Valid ReservationDto reservationDto, User user) {
        // request 로 넘어온 user 의 id와 예약 시간을 확인하고 해당 예약 내역이 있다면 삭제 처리, 없다면 예외 발생
        List<Reservation> existReservation = reservationRepository.findAllByUser_IdAndStore_IdAndReservationTime(
                user.getId(), reservationDto.getStoreId(), reservationDto.getReservationTime());
        if (existReservation.isEmpty()) {
            throw new StoreException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        reservationRepository.deleteAll(existReservation);
    }

    // 방문 예약 확인(예약 시간 10분 전보다 늦게 도착할 시 예외 발생)
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
