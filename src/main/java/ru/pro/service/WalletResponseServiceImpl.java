package ru.pro.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pro.mapper.WalletMapper;
import ru.pro.model.dto.WalletDto;
import ru.pro.model.entity.Wallet;
import ru.pro.model.enums.OperationType;
import ru.pro.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;

import static ru.pro.utils.Parser.UUIDParse;
import static ru.pro.utils.Parser.amountParse;
import static ru.pro.utils.Parser.operationTypeParse;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletResponseServiceImpl implements WalletResponseService {
    private final WalletRepository repository;
    private final WalletMapper mapper;

    @Override
    public WalletDto findById(String id) {
        UUID uuid = UUIDParse(id);
        return mapper.walletToWalletDto(getWalletOrThrow(uuid));
    }

    @Override
    @Transactional
    public WalletDto update(String walletId, String type, String amount) {
        UUID uuid = UUIDParse(walletId);
        OperationType operationType = operationTypeParse(type);
        BigDecimal sum = amountParse(amount);

        Wallet wallet = getWalletOrThrow(uuid);
        BigDecimal balance = wallet.getAmount();

        BigDecimal updatedAmount = operationType.apply(uuid, balance, sum);
        wallet.setAmount(updatedAmount);
        return mapper.walletToWalletDto(repository.save(wallet));
    }

    private Wallet getWalletOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found: " + id));
    }
}
