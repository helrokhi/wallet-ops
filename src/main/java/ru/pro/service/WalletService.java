package ru.pro.service;

import ru.pro.model.dto.WalletDto;
import ru.pro.model.dto.WalletRequest;

import java.util.UUID;

public interface WalletService {
    WalletDto updateWalletBalance(WalletRequest walletRequest);

    WalletDto getWalletBalance(UUID walletId);
}
