package com.huyntd.superapp.gundam_shop.service.message.impl;

import com.huyntd.superapp.gundam_shop.dto.request.MessageRequest;
import com.huyntd.superapp.gundam_shop.exception.AppException;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import com.huyntd.superapp.gundam_shop.mapper.MessageMapper;
import com.huyntd.superapp.gundam_shop.model.Conversation;
import com.huyntd.superapp.gundam_shop.model.Message;
import com.huyntd.superapp.gundam_shop.model.User;
import com.huyntd.superapp.gundam_shop.model.enums.UserRole;
import com.huyntd.superapp.gundam_shop.repository.ConversationRepository;
import com.huyntd.superapp.gundam_shop.repository.MessageRepository;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;
import com.huyntd.superapp.gundam_shop.service.message.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    final ConversationRepository conversationRepository;
    final UserRepository userRepository;

    final MessageRepository messageRepository;
    final MessageMapper messageMapper;


    @Override
    public Message save(MessageRequest request, int senderId) {

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Conversation conversation = null;
        int conversationId = request.getConversationId();
        log.info("conversationId: {}", conversationId);
        User staff = this.findUserHaveMinConversations();
        if(conversationId != -1){
            if(sender.getRole().equals(UserRole.CUSTOMER)){

                conversation = conversationRepository.save(Conversation.builder()
                                .customer(sender)
                                .staff(staff)
                                .build());
            }
        }else{
            conversation = conversationRepository.findById(conversationId)
                    .orElseThrow(() -> new RuntimeException("Conversation not found"));
        }





//        if (conversationRepository.isUserMemberOfConversation(conversationId, senderId)) {
//            throw new AppException(ErrorCode.CONVERSATION_ACCESS_DENIED);
//        }

        Message newMessage = messageMapper.toMessage(request);
        newMessage.setConversation(conversation);
        newMessage.setSender(sender);


        return messageRepository.save(newMessage);
    }


    public User findUserHaveMinConversations() {
        List<User> users = userRepository.findUsersOrderByConversationCountAsc();
        log.info("user found: {}", users.get(0));
        return users.get(0);
    }
}
