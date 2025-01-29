package com.zerobase.store_reservation.service;

import com.zerobase.store_reservation.dto.CreateStore;
import com.zerobase.store_reservation.dto.UpdateStore;
import com.zerobase.store_reservation.entity.Store;
import com.zerobase.store_reservation.exception.StoreException;
import com.zerobase.store_reservation.repository.StoreRepository;
import com.zerobase.store_reservation.type.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public void createStore(CreateStore.Request request) {
        Optional<Store> existStore = storeRepository.findByStoreName(request.getStoreName());
        if (existStore.isPresent()) {
            throw new StoreException(ErrorCode.STORE_ALREADY_REGISTRATION);
        }

        Store store = new Store(request);
        storeRepository.save(store);
    }

    @Transactional
    public void updateStore(UpdateStore.Request request) {
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));

        store.update(request);
        storeRepository.save(store);
    }

    public void deleteStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));
        storeRepository.deleteById(storeId);

    }
}
