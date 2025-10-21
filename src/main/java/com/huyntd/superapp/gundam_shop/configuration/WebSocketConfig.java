package com.huyntd.superapp.gundam_shop.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//@Configuration
//@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        // Có 2 loại destination: application destination (đầu vào), broker destination (dẫn message đến Broker Message)
//
//        // broker destination: server -> Broker (trong Spring hoặc bên ngoài) -> Clients
//        registry.enableSimpleBroker("/topic");
//        // application destination + với endpoint phía controller MessageMapping
//        // sẽ thành nơi client gọi đến server
//        registry.setApplicationDestinationPrefixes("/app");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//
//
//
//    }

}
