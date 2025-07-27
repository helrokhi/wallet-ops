package ru.pro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pro.exception.KafkaException;
import ru.pro.exception.answers.ErrorResponse;
import ru.pro.kafka.WalletProducer;
import ru.pro.kafka.WalletResponseConsumer;
import ru.pro.mapper.ExceptionMapper;
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
    private final ExceptionMapper exceptionMapper;

    @Override
    public WalletDto updateWalletBalance(WalletRequest request) {
        Object response = sendAndReceive(UPDATE, request);
        return handleResponse(response, request.getId());
    }

    @Override
    public WalletDto getWalletBalance(UUID walletId) {
        Object response = sendAndReceive(FIND_BY_ID, walletId);
        return handleResponse(response, walletId);
    }

    private Object sendAndReceive(String operation, Object payload) {
        consumer.prepareFutureResponse();
        producer.sendOperation(TOPIC, operation, payload);
        return consumer.getFutureResponse();
    }

    private WalletDto handleResponse(Object value, UUID walletId) {
        if (value instanceof WalletDto dto) {
            log.info("Обработан кошелек с ID {}", walletId);
            return objectMapper.convertValue(dto, WalletDto.class);
        }

        if (value instanceof EntityNotFoundException) {
            throw objectMapper.convertValue(value, EntityNotFoundException.class);
        }

        if (value instanceof ErrorResponse error) {
            throw exceptionMapper.toApiException(objectMapper.convertValue(error, ErrorResponse.class));
        }

        throw new KafkaException(TOPIC, "Неверный тип данных в ответе на запрос.");
    }
}
