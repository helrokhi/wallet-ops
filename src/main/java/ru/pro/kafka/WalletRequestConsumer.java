package ru.pro.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.pro.model.dto.ErrorEvent;
import ru.pro.model.dto.WalletDto;
import ru.pro.model.dto.WalletRequest;
import ru.pro.service.WalletResponseService;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY;
import static ru.pro.model.Constants.FIND_BY_ID;
import static ru.pro.model.Constants.TOPIC_RESPONSE;
import static ru.pro.model.Constants.UPDATE;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletRequestConsumer {
    private final WalletProducer producer;
    private final WalletResponseService service;

    @KafkaListener(
            topics = "wallet-operations-request",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "objectConcurrentKafkaListenerContainerFactory")
    public void listenForClientRequest(
            @Payload Object object,
            @Header(value = RECEIVED_KEY, required = false) String key,
            ConsumerRecord<String, Object> consumerRecord) {
        log.info("Received object: {}", object);
        log.info("consumerRecord.value() {}", consumerRecord.value());

        if (key == null) {
            log.warn("No key provided in Kafka message.");
            return;
        }

        handleRequest(key, consumerRecord.value());
    }

    private void handleRequest(String key, Object value) {
        switch (key) {
            case FIND_BY_ID -> handleFindById(value);
            case UPDATE -> handleUpdate(value);
            default -> log.warn("Unknown Kafka key: {}", key);
        }
    }

    private void handleFindById(Object value) {
        try {
            if (!(value instanceof String id)) {
                log.warn("Expected String for {}, got: {}", FIND_BY_ID, value.getClass());
                return;
            }
            log.info("Finding wallet by id: {}", id);
            WalletDto dto = service.findById(id);

            producer.sendOperation(TOPIC_RESPONSE, FIND_BY_ID, dto);
        } catch (Exception e) {
            handleError(FIND_BY_ID, e);
        }
    }

    private void handleUpdate(Object value) {
        try {
            if (!(value instanceof WalletRequest)) {
                log.warn("Expected WalletRequest, got: {}", value.getClass());
                return;
            }
            log.info("WalletRequest: {} Class {}", value, value.getClass());
            WalletRequest request = (WalletRequest) value;
            String walletId = request.getId();
            String type = request.getType();
            String amount = request.getAmount();

            WalletDto updatedDto = service.update(walletId, type, amount);

            log.info("Successfully updated wallet with id: {} {}", walletId, updatedDto);
            producer.sendOperation(TOPIC_RESPONSE, UPDATE, updatedDto);
        } catch (Exception e) {
            log.info("Exception -> {}", e.getClass());
            handleError(UPDATE, e);
        }

    }

    public void handleError(String key, Exception ex) {
        ErrorEvent event = new ErrorEvent(ex.getClass().getName(), ex.getMessage());
        producer.sendOperation(TOPIC_RESPONSE, key, event);
    }
}
