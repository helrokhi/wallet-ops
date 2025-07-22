package ru.pro.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.pro.model.dto.WalletDto;
import ru.pro.model.dto.WalletRequest;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

@RequestMapping("/api/v1")
public interface WalletApi {

    @RequestMapping(value = "/wallet", method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    default ResponseEntity<WalletDto> updateWalletBalance(@RequestBody WalletRequest walletRequest) {
        return new ResponseEntity<>(NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/wallets/{walletId}", method = RequestMethod.GET,
            produces = "application/json")
    default ResponseEntity<WalletDto> getWalletBalance(@PathVariable UUID walletId) {
        return new ResponseEntity<>(NOT_IMPLEMENTED);
    }
}
