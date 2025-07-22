package ru.pro.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.pro.model.entity.Wallet;
import ru.pro.model.enums.OperationType;
import ru.pro.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletUtils {
    private final WalletRepository walletRepository;

    public void processOperation(UUID walletId, OperationType type, BigDecimal amount) {
        Wallet wallet = walletRepository.findByIdForUpdate(walletId)
                .orElseGet(() -> createNewWallet(walletId));

        BigDecimal updatedAmount = type.apply(wallet.getAmount(), amount);

        if (updatedAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Баланс не может быть отрицательным");
        }

        wallet.setAmount(updatedAmount);
        walletRepository.save(wallet);
    }

    private Wallet createNewWallet(UUID walletId) {
        log.info("Кошелёк не найден, создаём новый: {}", walletId);
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setAmount(BigDecimal.ZERO);
        return walletRepository.save(wallet);
    }
}
