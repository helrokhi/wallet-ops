package ru.pro.exception;

import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;

public class InsufficientFundsException extends ApiException {
    public InsufficientFundsException(UUID walletId) {
        super(
                CONFLICT.value(),
                "INSUFFICIENT_FUNDS",
                "Недостаточно средств на кошельке",
                Map.of(
                        "walletId", walletId.toString()
                )
        );
    }
}
