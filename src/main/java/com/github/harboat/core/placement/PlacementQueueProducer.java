package com.github.harboat.core.placement;

import com.github.harboat.clients.placement.GeneratePlacement;
import com.github.harboat.rabbitmq.RabbitMQMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlacementQueueProducer {

    private final RabbitMQMessageProducer producer;

    @Value("${rabbitmq.exchanges.placement}")
    private String internalExchange;

    @Value("${rabbitmq.routing-keys.placement}")
    private String placementRoutingKey;

    public void send(GeneratePlacement generatePlacement) {
        producer.publish(generatePlacement, internalExchange, placementRoutingKey);
    }

}
