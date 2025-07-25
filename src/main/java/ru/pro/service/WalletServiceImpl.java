package ru.pro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pro.exception.InsufficientFundsException;
import ru.pro.exception.KafkaException;
import ru.pro.kafka.WalletProducer;
import ru.pro.kafka.WalletResponseConsumer;
import ru.pro.model.dto.WalletDto;
import ru.pro.model.dto.WalletRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {
    private static final String TOPIC = "wallet-operations-request";
    private static final String FIND_BY_ID = "findById";
    private static final String UPDATE = "update";

    private final WalletProducer producer;
    private final WalletResponseConsumer consumer;
    private final ObjectMapper objectMapper;

    @Override
    public WalletDto updateWalletBalance(WalletRequest request) {
        consumer.prepareFutureResponse();
        producer.sendOperation(TOPIC, UPDATE, request);

        Object value = consumer.getFutureResponse();

        if (value instanceof WalletDto) {
            WalletDto updatedDto = objectMapper.convertValue(value, WalletDto.class);
            log.info("Обновлен кошелек с ID {}", request.getId());
            return updatedDto;
        }

        if (value instanceof EntityNotFoundException) {
            throw objectMapper.convertValue(value, EntityNotFoundException.class);
        }

        if (value instanceof InsufficientFundsException) {
            throw objectMapper.convertValue(value, InsufficientFundsException.class);
        }
        throw new KafkaException(TOPIC, "Неверный тип данных в ответе на запрос.");
    }

    @Override
    public WalletDto getWalletBalance(UUID walletId) {
        consumer.prepareFutureResponse();
        producer.sendOperation(TOPIC, FIND_BY_ID, walletId);

        Object value = consumer.getFutureResponse();

        if (value instanceof WalletDto) {
            WalletDto dto = objectMapper.convertValue(value, WalletDto.class);
            log.info("Найден кошелек с ID {}", walletId);
            return dto;
        }

        if (value instanceof EntityNotFoundException) {
            throw objectMapper.convertValue(value, EntityNotFoundException.class);
        }
        throw new KafkaException(TOPIC, "Неверный тип данных в ответе на запрос.");
    }
}
