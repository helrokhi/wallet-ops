package ru.pro.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.pro.api.WalletApi;
import ru.pro.model.dto.WalletDto;
import ru.pro.model.dto.WalletRequest;
import ru.pro.service.WalletService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class WalletController implements WalletApi {
    private final WalletService service;

    @Override
    public ResponseEntity<WalletDto> updateWalletBalance(@RequestBody WalletRequest walletRequest) {
        WalletDto updatedWallet = service.updateWalletBalance(walletRequest);
        return ResponseEntity.ok(updatedWallet);
    }

    @Override
    public ResponseEntity<WalletDto> getWalletBalance(@PathVariable UUID walletId) {
        WalletDto walletDTO = service.getWalletBalance(walletId);
        return ResponseEntity.ok(walletDTO);
    }
}
