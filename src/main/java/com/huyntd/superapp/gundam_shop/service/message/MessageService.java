package com.huyntd.superapp.gundam_shop.service.message;

import com.huyntd.superapp.gundam_shop.dto.request.MessageRequest;
import com.huyntd.superapp.gundam_shop.dto.response.MessageResponse;
import com.huyntd.superapp.gundam_shop.dto.wsResponse.CountResponse;

import java.util.List;

public interface MessageService {
    MessageResponse save(MessageRequest request, int senderId);
    List<MessageResponse> getMessagesByConversationId(int conversationId);
    List<MessageResponse> getMessagesByCustomerId(int customerId);
    CountResponse countUnreadMessagesByReceiverId(int receiverId);
    CountResponse countReadMessagesByUserIdAndConversationId(int receiverId, int conversationId);
}
