package ru.pro.service;

import ru.pro.model.dto.WalletDto;
import ru.pro.model.dto.WalletRequest;

public interface WalletRequestService {
    WalletDto updateWalletBalance(WalletRequest walletRequest);

    WalletDto getWalletBalance(String walletId);
}
