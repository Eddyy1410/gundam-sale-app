package com.huyntd.superapp.gundam_shop.configuration;

import com.huyntd.superapp.gundam_shop.configuration.component.WebsocketAuthInterceptor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    final WebsocketAuthInterceptor stompAuthInterceptor;
    // Inject Interceptor

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Có 2 loại destination: application destination (đầu vào), broker destination (dẫn message đến Broker Message)

        // broker destination: server -> Broker (trong Spring hoặc bên ngoài) -> Clients
        // Định nghĩa tiền tố cho Message Broker (Server -> Client)
        // Ví dụ: Server sẽ gửi tin nhắn đến /topic/messages
        config.enableSimpleBroker("/topic", "/queue");


        // application destination + với endpoint phía controller MessageMapping
        // sẽ thành nơi client gọi đến server
        // Định nghĩa tiền tố cho Application (Client -> Server)
        // Ví dụ: Client gửi tin nhắn đến /app/chat
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Định nghĩa endpoint mà client sẽ kết nối
        // Sử dụng SockJS để hỗ trợ kết nối qua các trình duyệt cũ hơn (web)
        registry.addEndpoint("/ws-native") // HANDSHAKE endpoint
                .setAllowedOriginPatterns("*"); // Cho phép tất cả các nguồn (hoặc định nghĩa cụ thể)
                //.withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Đăng ký Interceptor để xác thực JWT khi client CONNECT
        registration.interceptors(stompAuthInterceptor);
    }

}
