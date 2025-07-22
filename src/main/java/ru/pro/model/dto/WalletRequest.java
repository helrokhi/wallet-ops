package ru.pro.model.dto;

import lombok.Data;
import ru.pro.model.enums.OperationType;

import java.util.UUID;

@Data
public class WalletRequest {
    private UUID id;
    private String amount;
    private OperationType type;
}
