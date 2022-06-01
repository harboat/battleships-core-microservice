package com.github.harboat.core;

import com.github.harboat.rabbitmq.RabbitMQMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameQueueProducer {

    private final RabbitMQMessageProducer producer;

    @Value("${rabbitmq.exchanges.game}")
    private String internalExchange;

    @Value("${rabbitmq.routing-keys.game}")
    private String gameCreationRoutingKey;

    @Async("gameQueueProducerThreads")
    public <T> void sendRequest(T request) {
        producer.publish(request, internalExchange, gameCreationRoutingKey);
    }

}
