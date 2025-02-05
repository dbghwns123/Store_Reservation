package com.zerobase.store_reservation.repository;

import com.zerobase.store_reservation.entity.Store;
import com.zerobase.store_reservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByStoreName(String storeName);

    Optional<Store> findByStoreNameAndUser(String storeName, User user);

    Optional<Store> findByIdAndUser(Long id, User user);

    List<Store> findAllByUser_Id(Long id);

}
