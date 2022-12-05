package fr.checkconsulting.gardeenfant.services;

import fr.checkconsulting.gardeenfant.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * BrokerProducerService
 */
@Slf4j
@Service
public class ProducerService {

    private final KafkaTemplate<String, Message> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, Message> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Message message) {
        // the KafkaTemplate provides asynchronous send methods returning a Future
        ListenableFuture<SendResult<String, Message>> future = kafkaTemplate.send(topic, message);

        // you can register a callback with the listener to receive the result of send asynchronously
        future.addCallback(new ListenableFutureCallback<SendResult<String, Message>>() {

            @Override
            public void onSuccess(SendResult<String, Message> result) {
                log.info("sent message='{}' with offset={}", message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.error("unable to send message='{}'", message, ex);
            }
        });
    }
}