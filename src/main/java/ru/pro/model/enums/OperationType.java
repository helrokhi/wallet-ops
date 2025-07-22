package ru.pro.model.enums;

import java.math.BigDecimal;

public enum OperationType {
    DEPOSIT {
        @Override
        public BigDecimal apply(BigDecimal balance, BigDecimal amount) {
            return balance.add(amount);
        }
    },
    WITHDRAW {
        @Override
        public BigDecimal apply(BigDecimal balance, BigDecimal amount) {
            return balance.subtract(amount);
        }
    };

    public abstract BigDecimal apply(BigDecimal balance, BigDecimal amount);
}
