package com.huyntd.superapp.gundam_shop.controller;

import com.huyntd.superapp.gundam_shop.configuration.CustomUserDetails;
import com.huyntd.superapp.gundam_shop.dto.request.MessageRequest;
import com.huyntd.superapp.gundam_shop.dto.response.MessageResponse;
import com.huyntd.superapp.gundam_shop.mapper.MessageMapper;
import com.huyntd.superapp.gundam_shop.model.Message;
import com.huyntd.superapp.gundam_shop.service.message.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
public class ChatController {

    MessageService messageService;

    // Dùng để gửi tin nhắn đến broker
    SimpMessagingTemplate messagingTemplate;

    MessageMapper messageMapper;

    @PreAuthorize("isAuthenticated()")
    @MessageMapping("/chat")
    public void handleChatMessage(
            MessageRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {

        int senderId = principal.getId();

        Message savedMessage = messageService.save(request, request.getConversationId(), senderId);

        MessageResponse response = messageMapper.toMessageResponse(savedMessage);

        String conversationTopic = "/topic/conversation/" + request.getConversationId();
        messagingTemplate.convertAndSend(conversationTopic, response);

    }

}
