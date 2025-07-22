package ru.pro.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ru.pro.model.enums.OperationType;

import java.util.UUID;

@Data
public class WalletRequest {
    @NotNull
    private UUID id;

    @NotBlank
    @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "Amount must be a number with max two decimal places")
    private String amount;

    @NotNull
    private OperationType type;
}
