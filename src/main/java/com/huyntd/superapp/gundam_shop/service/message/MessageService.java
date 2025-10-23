package com.huyntd.superapp.gundam_shop.service.message;

import com.huyntd.superapp.gundam_shop.dto.request.MessageRequest;
import com.huyntd.superapp.gundam_shop.model.Conversation;
import com.huyntd.superapp.gundam_shop.model.Message;

public interface MessageService {
    Message save(MessageRequest request, int conversationId, int senderId);
}
