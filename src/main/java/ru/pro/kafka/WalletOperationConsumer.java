package ru.pro.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.pro.model.dto.WalletRequest;
import ru.pro.utils.AmountParser;
import ru.pro.utils.WalletUtils;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletOperationConsumer {
    private final WalletUtils utils;
    private final AmountParser parser;

    @KafkaListener(topics = "wallet-operations", groupId = "wallet-balance-group")
    @Transactional
    public void handleOperation(WalletRequest request) {
        BigDecimal amount = parser.parse(request.getAmount());
        utils.processOperation(request.getId(), request.getType(), amount);
    }
}
