package com.huyntd.superapp.gundam_shop.service.message.impl;

import com.huyntd.superapp.gundam_shop.dto.request.MessageRequest;
import com.huyntd.superapp.gundam_shop.exception.AppException;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import com.huyntd.superapp.gundam_shop.mapper.MessageMapper;
import com.huyntd.superapp.gundam_shop.model.Conversation;
import com.huyntd.superapp.gundam_shop.model.Message;
import com.huyntd.superapp.gundam_shop.model.User;
import com.huyntd.superapp.gundam_shop.repository.ConversationRepository;
import com.huyntd.superapp.gundam_shop.repository.MessageRepository;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;
import com.huyntd.superapp.gundam_shop.service.message.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class MessageServiceImpl implements MessageService {

    ConversationRepository conversationRepository;
    UserRepository userRepository;

    MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public Message save(MessageRequest request, int conversationId, int senderId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        if (conversationRepository.isUserMemberOfConversation(conversationId, senderId)) {
            throw new AppException(ErrorCode.CONVERSATION_ACCESS_DENIED);
        }

        Message newMessage = messageMapper.toMessage(request);
        newMessage.setConversation(conversation);
        newMessage.setSender(sender);

        return messageRepository.save(newMessage);
    }
}
