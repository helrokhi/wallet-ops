package ru.pro.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.pro.model.dto.WalletRequest;

@Component
@RequiredArgsConstructor
public class WalletOperationProducer {
    private final KafkaTemplate<String, WalletRequest> kafkaTemplate;

    public void sendOperation(WalletRequest request) {
        kafkaTemplate.send("wallet-operations", request.getId().toString(), request);
    }
}
