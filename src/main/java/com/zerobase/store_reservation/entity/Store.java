package com.zerobase.store_reservation.entity;

import com.zerobase.store_reservation.dto.CreateStore;
import com.zerobase.store_reservation.dto.UpdateStore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Store extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "store")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "store")
    private List<Review> reviews;

    private String storeName;
    private String location;
    private String description;

    public Store(CreateStore.Request request, User user) {
        this.storeName = request.getStoreName();
        this.location = request.getLocation();
        this.description = request.getDescription();
        this.user = user;
    }

    public void update(UpdateStore.Request request) {
        this.storeName = request.getStoreName();
        this.location = request.getLocation();
        this.description = request.getDescription();
    }
}
