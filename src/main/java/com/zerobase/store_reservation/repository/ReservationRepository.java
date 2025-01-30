package com.zerobase.store_reservation.repository;

import com.zerobase.store_reservation.entity.Reservation;
import com.zerobase.store_reservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUser(User user);

}
