package com.huyntd.superapp.gundam_shop.controller;

import com.huyntd.superapp.gundam_shop.configuration.util.CustomUserDetails;
import com.huyntd.superapp.gundam_shop.dto.request.MessageRequest;
import com.huyntd.superapp.gundam_shop.dto.response.MessageResponse;
import com.huyntd.superapp.gundam_shop.mapper.MessageMapper;
import com.huyntd.superapp.gundam_shop.model.Message;
import com.huyntd.superapp.gundam_shop.service.message.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@Slf4j
public class ChatController {

    MessageService messageService;

    // Dùng để gửi tin nhắn đến broker
    SimpMessagingTemplate messagingTemplate;

    MessageMapper messageMapper;

    @PreAuthorize("isAuthenticated()")
    @MessageMapping("/chat/{customerId}")
    public void handleChatMessage(
            @DestinationVariable String customerId,
            MessageRequest request,
            // @AuthenticationPrincipal CustomUserDetails userDetail đã bị thay thế thành Principal
            // Lỗi ánh xạ
            Principal principal) {

        // 1. Ép kiểu Principal sang Authentication
        if (!(principal instanceof Authentication authentication)) {
            // Trường hợp không mong muốn, Principal không phải là Authentication Token
            log.error("Principal không phải là Authentication object");
            throw new AccessDeniedException("Principal not found or invalid type.");
        }

        // 2. Lấy CustomUserDetails từ Authentication object
        // Kiểm tra xem Principal trong Authentication có đúng kiểu không
        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            log.error("Authentication Principal không phải là CustomUserDetails");
            throw new AccessDeniedException("Invalid principal object.");
        }

        // 3. Kiểm tra tính toàn vẹn của DTO (Ngăn chặn NullPointerException)
        if (userDetails.getUserPrincipal() == null) {
            log.error("UserPrincipal bên trong CustomUserDetails bị mất (Deserialization error)");
            throw new AccessDeniedException("Session principal data corrupted.");
        }

        log.info(">>> handleChatMessage CALLED, principal = {}", principal);
        int senderId = userDetails.getId();

        MessageResponse savedMessage = messageService.save(request, senderId);

        String conversationTopic = "/topic/conversation/" + customerId;
        messagingTemplate.convertAndSend(conversationTopic, savedMessage);
    }



}
