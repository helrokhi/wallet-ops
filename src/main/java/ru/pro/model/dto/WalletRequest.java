package ru.pro.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalletRequest {
    @NotBlank(message = "Id cannot be blank")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "ID must be a valid UUID format")
    private String id;

    @NotBlank(message = "amount cannot be blank")
    @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "Amount must be a number with max two decimal places")
    private String amount;

    @NotBlank(message = "type cannot be blank")
    @Pattern(regexp = "DEPOSIT|WITHDRAW", message = "Operation type must be either DEPOSIT or WITHDRAW")
    private String type;
}
