package com.zerobase.store_reservation.repository;

import com.zerobase.store_reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUser_Id(Long userId);

    List<Reservation> findAllByUser_IdAndStore_IdAndReservationTime(Long userId, Long storeId, LocalDateTime reservationTime);

    Optional<Reservation> findByUser_IdAndStore_IdAndReservationTime(Long userId, Long storeId, LocalDateTime reservationTime);

    Optional<Reservation> findByIdAndUser_Id(Long id, Long userId);

    List<Reservation> findAllByStore_Id(Long storeId);
}
