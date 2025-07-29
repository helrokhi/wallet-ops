package ru.pro.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.pro.exception.KafkaException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static ru.pro.model.Constants.TOPIC_REQUEST;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletResponseConsumer {
    private volatile CompletableFuture<Object> futureResponse;

    public void prepareFutureResponse() {
        this.futureResponse = new CompletableFuture<>();
    }

    @KafkaListener(
            topics = "wallet-operations-response",
            groupId = "wallet-balance-group",
            containerFactory = "objectConcurrentKafkaListenerContainerFactory")
    @Transactional
    public void handleOperation(@Payload Object object,
                                ConsumerRecord<String, Object> consumerRecord) {
        log.info("Received object: {}", object);
        log.info("consumerRecord.value() {}", consumerRecord.value());

        Object value = consumerRecord.value();
        log.info("listen -> Extracted value from ConsumerRecord: {} class {}", value,
                (value != null) ? value.getClass() : "value is null");

        completeFutureResponse(value);
    }

    public Object getFutureResponse() {
        if (futureResponse == null) {
            log.warn("FutureResponse was not initialized.");
            return null;
        }

        try {
            Object result = futureResponse.get(10, SECONDS);
            log.info("<-FutureResponse: {}", result);
            return result;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Thread.currentThread().interrupt();
            throw new KafkaException(TOPIC_REQUEST, e.getMessage());
        }
    }

    private void completeFutureResponse(Object value) throws KafkaException {
        if (futureResponse != null) {
            futureResponse.complete(value);
            log.info("->FutureResponse completed with value: {}", value);
        } else {
            log.warn("Received Kafka response but futureResponse was null.");
        }
    }
}
