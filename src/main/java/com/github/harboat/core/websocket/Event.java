package com.github.harboat.core.websocket;

import com.github.harboat.clients.notification.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class Event<T> {
    private final EventType eventType;
    private final T content;
}
