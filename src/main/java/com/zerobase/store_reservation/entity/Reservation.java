package com.zerobase.store_reservation.entity;

import com.zerobase.store_reservation.type.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reservation extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private LocalDateTime reservationTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ReservationStatus status;

    public Reservation(User user, Store store, LocalDateTime reservationTime, ReservationStatus status) {
        this.user = user;
        this.store = store;
        this.reservationTime = reservationTime;
        this.status = status;
    }

    public void updateStatus(ReservationStatus reservationStatus) {
        this.status = reservationStatus;
    }
}
