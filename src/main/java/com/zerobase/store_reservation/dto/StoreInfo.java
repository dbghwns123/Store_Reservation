package com.zerobase.store_reservation.dto;

import com.zerobase.store_reservation.entity.Store;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreInfo {

    private Long storeId;
    private String name;
    private String location;
    private String description;

    public static StoreInfo fromEntity(Store store) {
        return StoreInfo.builder()
                .storeId(store.getId())
                .name(store.getStoreName())
                .location(store.getLocation())
                .description(store.getDescription())
                .build();
    }

}
