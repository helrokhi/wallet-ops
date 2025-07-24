package ru.pro.exception;

import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class KafkaSendException extends ApiException {
    public KafkaSendException(String topic, String message) {
        super(
                INTERNAL_SERVER_ERROR.value(),
                "KAFKA_SEND_ERROR",
                "Ошибка отправки сообщения в Kafka",
                Map.of(
                        "topic", topic,
                        "message", message
                )
        );
    }
}
