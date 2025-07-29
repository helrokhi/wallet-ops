package ru.pro.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pro.model.dto.WalletDto;
import ru.pro.model.dto.WalletRequest;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/api/v1")
public interface WalletApi {

    @RequestMapping(
            value = "/wallet",
            method = POST,
            consumes = "application/json",
            produces = "application/json")
    default ResponseEntity<WalletDto> updateWalletBalance(@RequestBody @Valid WalletRequest walletRequest) {
        return new ResponseEntity<>(NOT_IMPLEMENTED);
    }

    @RequestMapping(
            value = "/wallets/{walletId}",
            method = GET,
            produces = "application/json")
    default ResponseEntity<WalletDto> getWalletBalance(
            @PathVariable
            @NotBlank(message = "walletId cannot be blank")
            @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                    message = "ID must be a valid UUID format")
            String walletId) {
        return new ResponseEntity<>(NOT_IMPLEMENTED);
    }
}
