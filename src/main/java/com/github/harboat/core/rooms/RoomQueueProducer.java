package com.github.harboat.core.rooms;

import com.github.harboat.clients.rooms.ChangePlayerReadiness;
import com.github.harboat.clients.rooms.MarkStart;
import com.github.harboat.clients.rooms.RoomCreate;
import com.github.harboat.clients.rooms.RoomPlayerJoin;
import com.github.harboat.rabbitmq.RabbitMQMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomQueueProducer {

    private final RabbitMQMessageProducer producer;

    @Value("${rabbitmq.exchanges.rooms}")
    private String internalExchange;

    @Value("${rabbitmq.routing-keys.rooms}")
    private String roomRoutingKey;

    public void send(RoomCreate roomCreate) {
        producer.publish(roomCreate, internalExchange, roomRoutingKey);
    }


    public void send(ChangePlayerReadiness playerReadiness) {
        producer.publish(playerReadiness, internalExchange, roomRoutingKey);
    }

    public void send(MarkStart markStart) {
        producer.publish(markStart, internalExchange, roomRoutingKey);
    }

    public void send(RoomPlayerJoin roomPlayerJoin) {
        producer.publish(roomPlayerJoin, internalExchange, roomRoutingKey);
    }
}
