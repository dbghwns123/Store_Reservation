package com.zerobase.store_reservation.dto;

import com.zerobase.store_reservation.entity.Store;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerStoreInfo {

    private Long storeId;

    private String storeName;

    public static PartnerStoreInfo fromEntity(Store store) {
        return PartnerStoreInfo.builder()
                .storeId(store.getId())
                .storeName(store.getStoreName())
                .build();
    }

}
