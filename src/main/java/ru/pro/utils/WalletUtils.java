package ru.pro.utils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.pro.exception.InsufficientFundsException;
import ru.pro.mapper.WalletMapper;
import ru.pro.model.dto.WalletDto;
import ru.pro.model.entity.Wallet;
import ru.pro.model.enums.OperationType;
import ru.pro.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletUtils {
    private final WalletRepository repository;
    private final WalletMapper mapper;

    public void processOperation(UUID walletId, OperationType type, BigDecimal amount) {
        Wallet wallet = getWalletOrThrow(walletId);
        BigDecimal updatedAmount = type.apply(wallet.getAmount(), amount);
        log.info(String.valueOf(updatedAmount.compareTo(BigDecimal.ZERO) < 0));
        if (updatedAmount.compareTo(BigDecimal.ZERO) < 0) {
            log.info(String.valueOf(updatedAmount.compareTo(BigDecimal.ZERO) < 0));
            throw new InsufficientFundsException(walletId);
        }

        wallet.setAmount(updatedAmount);

        repository.save(wallet);
    }

    public Wallet getWalletOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found: " + id));
    }

    public WalletDto toDto(Wallet wallet) {
        return mapper.walletToWalletDto(wallet);
    }
}
