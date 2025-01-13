package com.dev.eventmanager.kafkaEvent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventMessageSender {
    private static final Logger log = LoggerFactory.getLogger(EventMessageSender.class);

    private final KafkaTemplate<Long, EventKafkaMessage> kafkaTemplate;

    public EventMessageSender(KafkaTemplate<Long, EventKafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(EventKafkaMessage eventKafkaMessage) {
        log.info("Sending message about event: message = {}", eventKafkaMessage);
        var result = kafkaTemplate.send(
                "event-topic",
                eventKafkaMessage.eventId(),
                eventKafkaMessage
        );
        result.thenAccept(sendResult -> {
            log.info("Send successful");
        });
    }

}
