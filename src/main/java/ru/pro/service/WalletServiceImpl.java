package ru.pro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pro.kafka.WalletOperationProducer;
import ru.pro.mapper.WalletMapper;
import ru.pro.model.dto.WalletDto;
import ru.pro.model.dto.WalletRequest;
import ru.pro.model.entity.Wallet;
import ru.pro.repository.WalletRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletOperationProducer producer;
    private final WalletRepository repository;
    private final WalletMapper mapper;

    @Override
    public WalletDto updateWalletBalance(WalletRequest request) {
        producer.sendOperation(request);
        return toDto(getWalletOrThrow(request.getId()));
    }

    @Override
    public WalletDto getWalletBalance(UUID walletId) {
        return toDto(getWalletOrThrow(walletId));
    }

    private Wallet getWalletOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found: " + id));
    }

    private WalletDto toDto(Wallet wallet) {
        return mapper.walletToWalletDto(wallet);
    }
}
