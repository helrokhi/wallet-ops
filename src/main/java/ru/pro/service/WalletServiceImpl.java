package ru.pro.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pro.kafka.WalletOperationProducer;
import ru.pro.model.dto.WalletDto;
import ru.pro.model.dto.WalletRequest;
import ru.pro.model.entity.Wallet;
import ru.pro.utils.WalletUtils;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {
    private final WalletOperationProducer producer;
    private final WalletUtils utils;

    @Override
    public WalletDto updateWalletBalance(WalletRequest request) {
        Wallet wallet = utils.getWalletOrThrow(request.getId());
        producer.sendOperation(request);
        return utils.toDto(wallet);
    }

    @Override
    public WalletDto getWalletBalance(UUID walletId) {
        return utils.toDto(utils.getWalletOrThrow(walletId));
    }
}
