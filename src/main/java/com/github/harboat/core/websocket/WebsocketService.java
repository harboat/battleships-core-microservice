package com.github.harboat.core.websocket;

import com.github.harboat.clients.notification.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
public class WebsocketService {
    private final SimpMessagingTemplate messagingTemplate;
    @Value("${broker.destination-prefix}")
    private String destination;

    @Async("websocketServiceThreads")
    public void notifyFrontEnd(String username, Event<?> event) {
        if (event.getEventType().equals(EventType.EXCEPTION)) {
            handleException(username, event);
            return;
        }
        messagingTemplate.convertAndSendToUser(username, destination, event);
    }

    private void handleException(String username, Event<?> event) {
        String message = ((LinkedHashMap<String, String>) event.getContent()).get("message");
        messagingTemplate.convertAndSendToUser(username, destination, new Exception(message));
    }
}
