package com.huyntd.superapp.gundam_shop.configuration.component;

import com.huyntd.superapp.gundam_shop.service.authentication.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class WebsocketAuthInterceptor implements ChannelInterceptor {

    final AuthenticationService authenticationService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("🧩 preSend triggered!");
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            log.info("⚠️ Không phải STOMP frame");
        } else {
            log.info("🧩 STOMP command: " + accessor.getCommand());
        }

        // Chỉ xử lý khi client gửi frame CONNECT (kết nối ban đầu)
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("Đã vào khu xử lý Frame CONNECT");


            // 1. Lấy Header "Authorization" từ Native Headers
            List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");
            log.info("Authorization headers: {}", authorizationHeaders);
            if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
                String fullToken = authorizationHeaders.get(0);

                if (fullToken.startsWith("Bearer ")) {
                    // Bắt đầu sau index 7 của chuỗi tức là sau lấy chuỗi sau "Bearer "
                    String jwt = fullToken.substring(7);

                    // Chỉ cần gọi getAuthentication, nó sẽ tự verify và tìm user
                    Authentication authentication = authenticationService.getAuthentication(jwt);
                    log.info("Auth resolved: {}", authentication != null ? authentication.getName() : "null");
                    if (authentication != null) {
                        // 3. Thiết lập Principal vào WebSocket Session
                        accessor.setUser(authentication);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                    // Nếu auth == null, không set user, Spring Security sẽ từ chối
                }
            } else {
                // Với các frame khác (SEND, SUBSCRIBE, ...), phải giữ nguyên user đã xác thực
                if (accessor.getUser() == null && SecurityContextHolder.getContext().getAuthentication() != null) {
                    accessor.setUser(SecurityContextHolder.getContext().getAuthentication());
                }
            }
        }

        // Trả lại message để tiếp tục xử lý
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        log.info("📦 postSend triggered!");
    }

}
