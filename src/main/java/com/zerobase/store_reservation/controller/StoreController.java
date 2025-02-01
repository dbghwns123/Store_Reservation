package com.zerobase.store_reservation.controller;

import com.zerobase.store_reservation.dto.CreateStore;
import com.zerobase.store_reservation.dto.StoreInfo;
import com.zerobase.store_reservation.dto.UpdateStore;
import com.zerobase.store_reservation.entity.Store;
import com.zerobase.store_reservation.security.UserDetailsImpl;
import com.zerobase.store_reservation.service.StoreService;
import com.zerobase.store_reservation.type.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreService storeService;

    // 사용자가 매장 정보 조회하는 API (단순 조회 기능이므로 아무나 사용할 수 있음)
    @GetMapping("/stores")
    public ResponseEntity<?> getStoreInfo(@RequestParam String storeName) {
        var result = storeService.getStoreInfo(storeName);

        return ResponseEntity.ok(result);
    }

    // 점주가 자신의 가게 목록을 조회하는 API(점주만 사용 가능한 기능)
    @GetMapping("/stores/partner")
    public ResponseEntity<?> getPartnerStores(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        var result = storeService.getPartnerStores(userDetails.getUser());
        return ResponseEntity.ok(result);
    }

    // 매장 등록 API
    @PostMapping("/stores")
    public ResponseEntity<String> createStore(@RequestBody @Valid CreateStore.Request request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (!userDetails.getUser().getUserRole().equals(UserRole.PARTNER)) {
            throw new AccessDeniedException("접근 권한이 없습니다");
        }

        storeService.createStore(request, userDetails.getUser());
        return ResponseEntity.ok("매장 등록을 완료하였습니다");
    }

    // 매장 정보 수정 API
    @PutMapping("/stores")
    public ResponseEntity<String> updateStore(@RequestBody @Valid UpdateStore.Request request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

//        boolean result = false;
//
//        for (Store s : userDetails.getUser().getStores()) {
//            if (s.getId() == request.getStoreId() && userDetails.getUser().getUserRole().equals(UserRole.PARTNER)) {
//                result = true;
//                break;
//            }
//        }
//        if (!result) {
//            throw new AccessDeniedException("접근 권한이 없습니다");
//        }

//        boolean hasPermission = userDetails.getUser().getUserRole().equals(UserRole.PARTNER) &&
//                userDetails.getUser().getStores().stream()
//                        .anyMatch(store -> Objects.equals(store.getId(), request.getStoreId()));
//
//        if (!hasPermission) {
//            throw new AccessDeniedException("접근 권한이 없습니다");
//        }

        if (!userDetails.getUser().getUserRole().equals(UserRole.PARTNER)) {
            throw new AccessDeniedException("접근 권한이 없습니다");
        }
        storeService.updateStore(request, userDetails.getUser());
        return ResponseEntity.ok("매장 정보 수정을 완료하였습니다.");
    }

    // 매장 등록 삭제 API
    @DeleteMapping("/stores")
    public ResponseEntity<String> deleteStore(@RequestParam Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

//        boolean hasPermission = userDetails.getUser().getUserRole().equals(UserRole.PARTNER) &&
//                userDetails.getUser().getStores().stream()
//                        .anyMatch(store -> Objects.equals(store.getId(), storeId));
//
//        if (!hasPermission) {
//            throw new AccessDeniedException("접근 권한이 없습니다");
//        }

        if (!userDetails.getUser().getUserRole().equals(UserRole.PARTNER)) {
            throw new AccessDeniedException("접근 권한이 없습니다");
        }

        storeService.deleteStore(storeId, userDetails.getUser());
        return ResponseEntity.ok("매장 정보 삭제를 완료하였습니다.");
    }
}
