package com.zerobase.store_reservation.repository;

import com.zerobase.store_reservation.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByStoreName(String storeName);

    Optional<Store> findByStoreIdAndStoreName(Long storeId, String storeName);
}
