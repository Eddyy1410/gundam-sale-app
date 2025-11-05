package com.huyntd.superapp.gundam_shop.controller;

import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import com.huyntd.superapp.gundam_shop.dto.request.UpdateReadMessageRequest;
import com.huyntd.superapp.gundam_shop.dto.response.MessageResponse;
import com.huyntd.superapp.gundam_shop.dto.wsResponse.CountResponse;
import com.huyntd.superapp.gundam_shop.service.message.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    MessageService messageService;

    @GetMapping("/{customerId}")
    ApiResponse<List<MessageResponse>> getMessagesByCustomerId(@PathVariable int customerId) {
        return ApiResponse.<List<MessageResponse>>builder()
                .result(messageService.getMessagesByCustomerId(customerId))
                .build();
    }

    @GetMapping("/unread/{receiverId}")
    ApiResponse<CountResponse> countUnreadMessagesByReceiverId(@PathVariable int receiverId) {
        return ApiResponse.<CountResponse>builder()
                .result(messageService.countUnreadMessagesByReceiverId(receiverId))
                .build();
    }

    @PutMapping("/read")
    ApiResponse<CountResponse> updateReadMessages(@RequestBody UpdateReadMessageRequest request) {
        return ApiResponse.<CountResponse>builder()
                .result(messageService.countReadMessagesByUserIdAndConversationId(request.getReceiverId(), request.getConversationId()))
                .build();
    }
}
