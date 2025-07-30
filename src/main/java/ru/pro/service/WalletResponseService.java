package ru.pro.service;

import ru.pro.model.dto.WalletDto;

public interface WalletResponseService {
    WalletDto findById(String id);
    WalletDto update(String walletId, String type, String amount);
}
