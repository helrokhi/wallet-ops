package ru.pro.exception;

import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class KafkaException extends ApiException {
    public KafkaException(String topic, String message) {
        super(
                INTERNAL_SERVER_ERROR.value(),
                "KAFKA_RECEIVE_ERROR",
                "Ошибка получения/отправки сообщения в Kafka",
                Map.of(
                        "topic", topic,
                        "message", message
                )
        );
    }
}