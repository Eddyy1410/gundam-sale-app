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
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
    @MessageMapping("/chat")
    public void handleChatMessage(
            MessageRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {

        log.info(">>> handleChatMessage CALLED, principal = {}", principal);
        int senderId = principal.getId();

        Message savedMessage = messageService.save(request, senderId);

        MessageResponse response = messageMapper.toMessageResponse(savedMessage);

        String conversationTopic = "/topic/conversation/" + savedMessage.getConversation().getId();
        messagingTemplate.convertAndSend(conversationTopic, response);

    }



}
