package com.zerobase.store_reservation.service;

import com.zerobase.store_reservation.dto.CreateStore;
import com.zerobase.store_reservation.dto.PartnerStoreInfo;
import com.zerobase.store_reservation.dto.StoreInfo;
import com.zerobase.store_reservation.dto.UpdateStore;
import com.zerobase.store_reservation.entity.Store;
import com.zerobase.store_reservation.entity.User;
import com.zerobase.store_reservation.exception.StoreException;
import com.zerobase.store_reservation.repository.StoreRepository;
import com.zerobase.store_reservation.repository.UserRepository;
import com.zerobase.store_reservation.type.ErrorCode;
import com.zerobase.store_reservation.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    // 사용자가 매장 정보 조회하는 API (단순 조회 기능이므로 아무나 사용할 수 있음)
    public StoreInfo getStoreInfo(String storeName) {
        Store existStore = storeRepository.findByStoreName(storeName)
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));
        return StoreInfo.fromEntity(existStore);
    }

    // 점주가 자신의 가게 목록을 조회하는 API(점주만 사용 가능한 기능)
    public List<PartnerStoreInfo> getPartnerStores(User user) {
        // 로그인이 문제없이 되었다는건 회원가입이 되어있다는 것이므로 get 으로 바로 가져옴
        User userById = userRepository.findById(user.getId()).get();
        // 찾은 유저의 role 이 partner 인지 확인(일반 유저라면 권한 없음)
        if (userById.getUserRole().equals(UserRole.USER)) {
            throw new StoreException(ErrorCode.NO_PERMISSION);
        }
        // 해당 파트너의 아이디를 가진 store 정보 있는지 확인
        List<Store> stores = storeRepository.findAllByUser_Id(userById.getId());
        return stores.stream()
                .map(PartnerStoreInfo::fromEntity)
                .collect(Collectors.toList());
    }

    // 매장 등록 API
    public void createStore(CreateStore.Request request, User user) {
        // request로 매장 이름과 user 정보가 넘어오고 그걸로 이미 등록된 가게 정보가 있는지 확인
        Optional<Store> existStore = storeRepository.findByStoreNameAndUser(request.getStoreName(), user);
        if (existStore.isPresent()) {
            throw new StoreException(ErrorCode.STORE_ALREADY_REGISTRATION);
        }

        Store store = new Store(request, user);
        storeRepository.save(store);
    }

    // 매장 정보 수정 API
    @Transactional
    public void updateStore(UpdateStore.Request request, User user) {
        Store store = storeRepository.findByIdAndUser(request.getStoreId(), user)
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));

        store.update(request);
        storeRepository.save(store);
    }

    // 매장 등록 삭제 API
    public void deleteStore(Long storeId, User user) {
        Store store = storeRepository.findByIdAndUser(storeId, user)
                .orElseThrow(() -> new StoreException(ErrorCode.STORE_NOT_FOUND));
        storeRepository.deleteById(storeId);

    }


}
