package ru.pro.utils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
    private final AmountParser parser;

    public WalletDto findById(UUID id) {
        return mapper.walletToWalletDto(getWalletOrThrow(id));
    }

    @Transactional
    public WalletDto update(UUID walletId, OperationType type, String amount) {
        Wallet wallet = getWalletOrThrow(walletId);
        BigDecimal sum = parser.parse(amount);
        BigDecimal updatedAmount = type.apply(wallet.getAmount(), sum);
        boolean isValid = isValid(updatedAmount);
        log.info(String.valueOf(isValid));
        if (isValid(updatedAmount)) {
            log.info(String.valueOf(isValid));
            throw new InsufficientFundsException(walletId);
        }

        wallet.setAmount(updatedAmount);

        return mapper.walletToWalletDto(repository.save(wallet));
    }

    private Wallet getWalletOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found: " + id));
    }

    private boolean isValid(BigDecimal updatedAmount) {
        return updatedAmount.compareTo(BigDecimal.ZERO) < 0;
    }
}
