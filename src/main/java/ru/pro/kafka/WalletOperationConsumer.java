package ru.pro.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
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

    @KafkaListener(
            topics = "wallet-operations",
            groupId = "wallet-balance-group",
            containerFactory = "objectConcurrentKafkaListenerContainerFactory")
    @Transactional
    public void handleOperation(@Payload Object object,
                                ConsumerRecord<String, Object> consumerRecord) {
        log.info("Received object: {}", object);
        log.info("consumerRecord.value() {}", consumerRecord.value());

        Object input = consumerRecord.value();
        if (!(input instanceof WalletRequest request)) {
            throw new RuntimeException("Ожидался WalletRequest");
        }
        BigDecimal amount = parser.parse(request.getAmount());
        utils.processOperation(request.getId(), request.getType(), amount);
    }
}
