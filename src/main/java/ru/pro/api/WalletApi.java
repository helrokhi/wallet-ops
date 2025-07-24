package ru.pro.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pro.model.dto.WalletDto;
import ru.pro.model.dto.WalletRequest;

import java.util.UUID;

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
    default ResponseEntity<WalletDto> getWalletBalance(@PathVariable @Valid UUID walletId) {
        return new ResponseEntity<>(NOT_IMPLEMENTED);
    }
}
