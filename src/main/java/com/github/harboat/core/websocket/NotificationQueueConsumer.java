package com.github.harboat.core.websocket;

import com.github.harboat.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationQueueConsumer {
    private WebsocketService service;

    @RabbitListener(
            queues = {"${rabbitmq.queues.notification}"}
    )
    void consume(NotificationRequest<?> request) {
        service.notifyFrontEnd(request.userId(), new Event<>(request.type(), request.body()));
    }

}
