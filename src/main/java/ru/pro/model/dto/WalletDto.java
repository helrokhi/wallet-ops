package ru.pro.model.dto;

import ru.pro.model.enums.OperationType;

import java.util.UUID;

public record WalletDto(UUID id, String amount, OperationType type) {
}
