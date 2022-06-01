package com.github.harboat.core.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Value("${broker.application-destination-prefix}")
    private String destinationPrefix;

    @Value("${broker.endpoint}")
    private String endpoint;

    @Value("${broker.relay-host}")
    private String relayHost;

    @Value("${broker.relay-port}")
    private Integer relayPort;

    @Value("${broker.client-login}")
    private String clientLogin;

    @Value("${broker.client-passcode}")
    private String clientPasscode;

    @Value("${broker.system-login}")
    private String systemLogin;

    @Value("${broker.system-passcode}")
    private String systemPasscode;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry
                .enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(relayHost)
                .setRelayPort(relayPort)
                .setClientLogin(clientLogin)
                .setClientPasscode(clientPasscode)
                .setSystemLogin(systemLogin)
                .setSystemPasscode(systemPasscode);
        registry.setApplicationDestinationPrefixes(destinationPrefix);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint(endpoint)
                    .withSockJS();
    }
}
