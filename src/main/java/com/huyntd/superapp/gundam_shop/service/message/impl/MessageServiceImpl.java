package com.huyntd.superapp.gundam_shop.service.message.impl;

import com.huyntd.superapp.gundam_shop.dto.enums.CountType;
import com.huyntd.superapp.gundam_shop.dto.request.MessageRequest;
import com.huyntd.superapp.gundam_shop.dto.response.MessageResponse;
import com.huyntd.superapp.gundam_shop.dto.wsResponse.CountResponse;
import com.huyntd.superapp.gundam_shop.exception.AppException;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import com.huyntd.superapp.gundam_shop.mapper.MessageMapper;
import com.huyntd.superapp.gundam_shop.model.Conversation;
import com.huyntd.superapp.gundam_shop.model.Message;
import com.huyntd.superapp.gundam_shop.model.User;
import com.huyntd.superapp.gundam_shop.model.enums.MessageStatus;
import com.huyntd.superapp.gundam_shop.model.enums.UserRole;
import com.huyntd.superapp.gundam_shop.repository.ConversationRepository;
import com.huyntd.superapp.gundam_shop.repository.MessageRepository;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;
import com.huyntd.superapp.gundam_shop.service.message.MessageService;
import com.huyntd.superapp.gundam_shop.service.wsNotification.WsNotificationService;
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
    final WsNotificationService wsNotificationService;

    public User findUserHaveMinConversations() {
        List<User> users = userRepository.findUsersOrderByConversationCountAsc();
        log.info("user found: {}", users.get(0));
        return users.get(0);
    }

    public int determineReceiverId(int conversationId, int senderId) {
        // Giả định bạn có ConversationRepository tiêm vào
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        if (conversation.getCustomer().getId() == senderId) {
            // Nếu người gửi là Customer, người nhận là Staff
            return conversation.getStaff().getId();
        } else if (conversation.getStaff().getId() == senderId) {
            // Nếu người gửi là Staff, người nhận là Customer
            return conversation.getCustomer().getId();
        } else {
            throw new RuntimeException("Sender is not a participant in this conversation.");
        }
    }

    @Override
    public MessageResponse save(MessageRequest request, int senderId) {

        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_EXISTED));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Message savedMessage = messageRepository.save(Message.builder()
                        .content(request.getContent())
                        .sender(sender)
                        .conversation(conversation)
                        .status(MessageStatus.UNREAD)
                        .build());

        int receiverId = determineReceiverId(savedMessage.getConversation().getId(), senderId);

        // 3. TÍNH TOÁN BADGE MỚI CHO RECEIVER
        int newUnreadCount = messageRepository.countUnreadMessagesForUser(receiverId);
        wsNotificationService.sendUnreadCountUpdate(receiverId, newUnreadCount, CountType.UNREAD_MESSAGE);

        return messageMapper.toMessageResponse(savedMessage);
    }

    @Override
    public List<MessageResponse> getMessagesByConversationId(int conversationId) {
        return messageRepository.findMessagesByConversationId(conversationId)
                .stream()
                .map(messageMapper::toMessageResponse)
                .toList();
    }

    @Override
    public List<MessageResponse> getMessagesByCustomerId(int customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (!customer.getRole().equals(UserRole.CUSTOMER)) throw new AppException(ErrorCode.CUSTOMER_NOT_EXISTED);
        int conversationId = conversationRepository.findConversationIdByCustomerId(customerId).orElse(-1);
        // Nếu chưa có conversation của customer -> (id = -1) thì tạo 1 conversation
        // Staff lấy id thằng ít conversation list nhất
//        if (conversationId == -1) {
//            Conversation c = conversationRepository.save(Conversation.builder()
//                    .customer(customer)
//                    .staff(userRepository.findById(userRepository.findLeastBusyStaffId().get())
//                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)))
//                    .status(ConversationStatus.NEW)
//                    .build());
//
//            return messageRepository.findMessagesByConversationId(c.getId())
//                    .stream()
//                    .map(messageMapper::toMessageResponse)
//                    .toList();
//        }
        return messageRepository.findMessagesByConversationId(conversationId)
                .stream()
                .map(messageMapper::toMessageResponse)
                .toList();
    }

    @Override
    public CountResponse countUnreadMessagesByReceiverId(int receiverId) {
        return CountResponse.builder()
                .count(messageRepository.countUnreadMessagesForUser(receiverId))
                .type(CountType.UNREAD_MESSAGE)
                .build();
    }

    @Override
    public CountResponse countReadMessagesByUserIdAndConversationId(int receiverId, int conversationId) {
        return CountResponse.builder()
                .count(messageRepository.markMessagesAsReadInConversation(receiverId, conversationId))
                .type(CountType.READ_MESSAGE)
                .build();
    }


}
