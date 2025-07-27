package ru.pro.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.pro.exception.KafkaException;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOperation(String topic, String message, Object object) {
        log.info("send -> topic: {} message: {} object: {}", topic, message, object);
        kafkaTemplate.send(topic, message, object);
//                .whenComplete((result, ex) -> {
//                    if (ex != null) {
//                        throw new KafkaException("Failed to send message to Kafka", topic);
//                    } else {
//                        log.info("Message {} sent successfully to topic: {}", message, topic);
//                    }
//                });
    }
}
