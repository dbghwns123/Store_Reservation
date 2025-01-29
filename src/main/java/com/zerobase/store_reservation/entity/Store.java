package com.zerobase.store_reservation.entity;

import com.zerobase.store_reservation.dto.CreateStore;
import com.zerobase.store_reservation.dto.UpdateStore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Store extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    private String storeName;
    private String location;
    private String description;

    public Store(CreateStore.Request request) {
        this.storeName = request.getStoreName();
        this.location = request.getLocation();
        this.description = request.getDescription();
    }

    public void update(UpdateStore.Request request) {
        this.storeName = request.getStoreName();
        this.location = request.getLocation();
        this.description = request.getDescription();
    }
}
