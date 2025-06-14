package org.neb.fluxin.adapter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestConsumer {

    @KafkaListener(topics = "test",
               groupId = "console-consumer-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            Header typeHeader = record.headers().lastHeader("type");
            String typeValue = typeHeader != null ? new String(typeHeader.value(), StandardCharsets.UTF_8) : "Unknown";
            log.info("Received header type: {}", typeValue);

            // Convert DynamicMessage → JSON → Java Object
            String message = record.value();

            log.info("Received message: {}", message);

        } catch (Exception e) {
            log.error("Failed to consume message", e);
        }
    }

}
