package com.zerobase.store_reservation.service;

import com.zerobase.store_reservation.dto.CreateStore;
import com.zerobase.store_reservation.dto.UpdateStore;
import com.zerobase.store_reservation.entity.Store;
import com.zerobase.store_reservation.entity.User;
import com.zerobase.store_reservation.exception.StoreException;
import com.zerobase.store_reservation.repository.StoreRepository;
import com.zerobase.store_reservation.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public void createStore(CreateStore.Request request, User user) {
        Optional<Store> existStore = storeRepository.findByStoreNameAndUser(request.getStoreName(), user);
        if (existStore.isPresent()) {
            throw new StoreException(ErrorCode.STORE_ALREADY_REGISTRATION);
        }

        Store store = new Store(request, user);
        storeRepository.save(store);
    }

    @Transactional
    public void updateStore(UpdateStore.Request request, User user) {
        Store store = storeRepository.findByIdAndUser(request.getStoreId(), user)
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));

        store.update(request);
        storeRepository.save(store);
    }

    public void deleteStore(Long storeId, User user) {
        Store store = storeRepository.findByIdAndUser(storeId, user)
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));
        storeRepository.deleteById(storeId);

    }
}
