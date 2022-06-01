package com.github.harboat.core.configuration;

import com.github.harboat.clients.configuration.SetGameSize;
import com.github.harboat.rabbitmq.RabbitMQMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfigurationQueueProducer {

    private final RabbitMQMessageProducer producer;

    @Value("${rabbitmq.exchanges.config}")
    private String internalExchange;

    @Value("${rabbitmq.routing-keys.config}")
    private String configRoutingKey;

    public void send(SetGameSize setGameSize) {
        producer.publish(setGameSize, internalExchange, configRoutingKey);
    }
}
