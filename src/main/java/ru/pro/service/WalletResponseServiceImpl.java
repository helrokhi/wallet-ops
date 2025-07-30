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
import ru.pro.utils.AmountParser;
import ru.pro.utils.IdParser;
import ru.pro.utils.OperationTypeParser;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletResponseServiceImpl implements WalletResponseService {
    private final WalletRepository repository;
    private final WalletMapper mapper;
    private final AmountParser parser;
    private final IdParser idParser;
    private final OperationTypeParser typeParser;

    @Override
    public WalletDto findById(String id) {
        UUID uuid = idParser.parse(id);
        return mapper.walletToWalletDto(getWalletOrThrow(uuid));
    }

    @Override
    @Transactional
    public WalletDto update(String walletId, String type, String amount) {
        UUID uuid = idParser.parse(walletId);
        OperationType operationType = typeParser.parse(type);
        BigDecimal sum = parser.parse(amount);

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
