package com.huyntd.superapp.gundam_shop.controller;

import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import com.huyntd.superapp.gundam_shop.dto.response.MessageResponse;
import com.huyntd.superapp.gundam_shop.service.message.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
