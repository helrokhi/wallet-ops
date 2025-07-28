package ru.pro.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ru.pro.model.enums.OperationType;

import java.util.UUID;

@Data
public class WalletRequest {
    @NotBlank
    @Pattern(regexp ="^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "ID must be a valid UUID format")
    private String id;

    @NotBlank
    @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "Amount must be a number with max two decimal places")
    private String amount;

    @NotBlank
    @Pattern(regexp = "DEPOSIT|WITHDRAW", message = "Operation type must be either DEPOSIT or WITHDRAW")
    private String type;
}
