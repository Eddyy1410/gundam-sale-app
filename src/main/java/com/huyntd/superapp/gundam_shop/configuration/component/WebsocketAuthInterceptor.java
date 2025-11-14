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

import java.security.Principal;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class WebsocketAuthInterceptor implements ChannelInterceptor {

    final AuthenticationService authenticationService;
    static final String AUTHORIZATION_HEADER = "Authorization";
    static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("🧩 preSend triggered!");
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            log.info("⚠️ Không phải STOMP frame");
            return message;
        } else {
            log.info("🧩 STOMP command: " + accessor.getCommand());
        }

        // --- 1. XỬ LÝ FRAME CONNECT (Xác thực JWT và thiết lập Principal cho Session) ---
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("Đã vào khu xử lý Frame CONNECT");

            // 1. Lấy Header "Authorization" từ Native Headers
            List<String> authorizationHeaders = accessor.getNativeHeader(AUTHORIZATION_HEADER);
            log.info("Authorization headers: {}", authorizationHeaders);
            if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
                String fullToken = authorizationHeaders.get(0);

                if (fullToken.startsWith(BEARER_PREFIX)) {
                    // Bắt đầu sau index 7 của chuỗi tức là sau lấy chuỗi sau "Bearer "
                    String jwt = fullToken.substring(BEARER_PREFIX.length());

                    try {
                        Authentication authentication = authenticationService.getAuthentication(jwt);
                        log.info("Auth resolved: {}", authentication != null ? authentication.getName() : "null");
                        if (authentication != null) {
                            // Gán Principal vào STOMP Session (Cơ chế lưu trữ trạng thái lâu dài)
                            accessor.setUser(authentication);
                            log.info("WebSocket CONNECT thành công cho user: {}", authentication.getName());
                            // Thiết lập ContextHolder TẠM THỜI cho frame CONNECT (nếu cần xử lý ngay)
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    } catch (Exception e) {
                        // Xử lý lỗi xác thực (ví dụ: token hết hạn, token không hợp lệ)
                        log.error("Lỗi xác thực STOMP CONNECT: {}", e.getMessage());
                        // Có thể ném ngoại lệ ở đây để từ chối kết nối
                        // throw new AccessDeniedException("Invalid or expired token.");
                    }
                }
            }
        }

        // --- BƯỚC 2: TÁI TẠO SECURITY CONTEXT TỪ STOMP SESSION ---
        // Áp dụng cho mọi frame (CONNECT, SEND, SUBSCRIBE)

        // Lấy Principal từ STOMP Session (đã được Spring khôi phục nếu không phải CONNECT)
        Principal principal = accessor.getUser();

        // Nếu đã có Principal được thiết lập trong Session (qua setUser), ta khôi phục ContextHolder
        if (principal instanceof Authentication) {
            // Đặt Authentication vào ContextHolder của luồng hiện tại
            SecurityContextHolder.getContext().setAuthentication((Authentication) principal);
            log.info("Khôi phục SecurityContext cho Frame {}: {}", accessor.getCommand(), ((Authentication) principal).getPrincipal().toString());
        }
        // Nếu không có Principal, SecurityContextHolder vẫn rỗng (chính xác nếu user chưa login)

        // Trả lại message để tiếp tục xử lý
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        log.info("📦 postSend triggered!\n");
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null) {
            // Dọn dẹp ThreadLocal Context Holder
            SecurityContextHolder.clearContext();
            log.debug("Dọn dẹp SecurityContext sau khi xử lý frame: {}", accessor.getCommand());
        }
    }

}
