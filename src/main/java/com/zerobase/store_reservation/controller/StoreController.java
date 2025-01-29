package com.zerobase.store_reservation.controller;

import com.zerobase.store_reservation.dto.CreateStore;
import com.zerobase.store_reservation.dto.UpdateStore;
import com.zerobase.store_reservation.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/stores")
    public ResponseEntity<String> createStore(@RequestBody @Valid CreateStore.Request request) {
        storeService.createStore(request);
        return ResponseEntity.ok("매장 등록을 완료하였습니다");
    }

    @PutMapping("/stores")
    public ResponseEntity<String> updateStore(@RequestBody @Valid UpdateStore.Request request) {
        storeService.updateStore(request);
        return ResponseEntity.ok("매장 정보 수정을 완료하였습니다.");
    }

    @DeleteMapping("/stores")
    public ResponseEntity<String> deleteStore(@RequestParam Long storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.ok("매장 정보 삭제를 완료하였습니다.");
    }
}
