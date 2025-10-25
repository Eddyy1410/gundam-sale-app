package com.huyntd.superapp.gundam_shop.service.message;

import com.huyntd.superapp.gundam_shop.dto.request.MessageRequest;
import com.huyntd.superapp.gundam_shop.dto.response.MessageResponse;
import com.huyntd.superapp.gundam_shop.model.Conversation;
import com.huyntd.superapp.gundam_shop.model.Message;

import java.util.List;

public interface MessageService {
    Message save(MessageRequest request, int senderId);
    List<MessageResponse> getMessagesByConversationId(int conversationId);
    List<MessageResponse> getMessagesByCustomerId(int customerId);
}
