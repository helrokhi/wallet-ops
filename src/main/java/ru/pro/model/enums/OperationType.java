package ru.pro.model.enums;

import lombok.extern.slf4j.Slf4j;
import ru.pro.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
public enum OperationType {
    DEPOSIT {
        @Override
        public BigDecimal apply(UUID walletId, BigDecimal balance, BigDecimal amount) {
            return balance.add(amount);
        }
    },
    WITHDRAW {
        @Override
        public BigDecimal apply(UUID walletId, BigDecimal balance, BigDecimal amount) {
            BigDecimal updatedAmount = balance.subtract(amount);
            if (updatedAmount.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFundsException("Недостаточно средств в кошельке " + walletId);
            }
            return updatedAmount;
        }
    };

    public abstract BigDecimal apply(UUID walletId, BigDecimal balance, BigDecimal amount);
}
