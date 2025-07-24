package ru.pro.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.pro.exception.KafkaSendException;
import ru.pro.model.dto.WalletRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletOperationProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOperation(WalletRequest request) {
        log.info("send -> topic: {} message: {} object: {}", "wallet-operations", request.getId().toString(), request);
        kafkaTemplate.send("wallet-operations", request.getId().toString(), request)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        throw new KafkaSendException("Failed to send message to Kafka", "wallet-operations");
                    } else {
                        log.info("Message sent successfully to topic: wallet-operations");
                    }
                });
    }
}
