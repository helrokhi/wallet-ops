package ru.pro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pro.exception.KafkaException;
import ru.pro.kafka.WalletProducer;
import ru.pro.kafka.WalletResponseConsumer;
import ru.pro.model.dto.ErrorEvent;
import ru.pro.model.dto.WalletDto;
import ru.pro.model.dto.WalletRequest;

import static ru.pro.model.Constants.FIND_BY_ID;
import static ru.pro.model.Constants.TOPIC_REQUEST;
import static ru.pro.model.Constants.UPDATE;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletRequestServiceImpl implements WalletRequestService {
    private final WalletProducer producer;
    private final WalletResponseConsumer consumer;
    private final ObjectMapper objectMapper;

    @Override
    public WalletDto updateWalletBalance(WalletRequest request) {
        Object response = sendAndReceive(UPDATE, request);
        return handleResponse(response, request.getId());
    }

    @Override
    public WalletDto getWalletBalance(String walletId) {
        Object response = sendAndReceive(FIND_BY_ID, walletId);
        return handleResponse(response, walletId);
    }

    private Object sendAndReceive(String operation, Object payload) {
        consumer.prepareFutureResponse();
        producer.sendOperation(TOPIC_REQUEST, operation, payload);
        return consumer.getFutureResponse();
    }

    private WalletDto handleResponse(Object value, String walletId) {
        if (value instanceof WalletDto dto) {
            log.info("Обработан кошелек с ID {}", walletId);
            return objectMapper.convertValue(dto, WalletDto.class);
        }

        if (value instanceof ErrorEvent event) {
            event.throwExceptionBasedOnError();
        }

        throw new KafkaException(TOPIC_REQUEST, "Неверный тип данных в ответе на запрос.");
    }
}
