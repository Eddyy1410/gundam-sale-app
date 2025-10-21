package com.huyntd.superapp.gundam_shop.controller;

import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import com.huyntd.superapp.gundam_shop.dto.response.ConversationResponse;
import com.huyntd.superapp.gundam_shop.service.conversation.ConversationService;
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
@RequestMapping("/chat")
public class ConversationController {

    ConversationService conversationService;

    @GetMapping("/{userId}/conversations")
    ApiResponse<List<ConversationResponse>> getConversationList(@PathVariable("userId") int staffId) {
        return ApiResponse.<List<ConversationResponse>>builder()
                .result(conversationService.findConversationListForStaff(staffId))
                .build();
    }

}
