package com.huyntd.superapp.gundam_shop.service.conversation.impl;

import com.huyntd.superapp.gundam_shop.dto.response.ConversationResponse;
import com.huyntd.superapp.gundam_shop.exception.AppException;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import com.huyntd.superapp.gundam_shop.model.Conversation;
import com.huyntd.superapp.gundam_shop.model.User;
import com.huyntd.superapp.gundam_shop.model.enums.ConversationStatus;
import com.huyntd.superapp.gundam_shop.repository.ConversationRepository;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;
import com.huyntd.superapp.gundam_shop.service.conversation.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ConversationServiceImpl implements ConversationService {

    ConversationRepository conversationRepository;
    UserRepository userRepository;

    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    @Override
    public List<ConversationResponse> findConversationListForStaff(int staffId) {
        return conversationRepository.findConversationListForStaff(staffId);
    }

    @Override
    public ConversationResponse findConversationByCustomerId(int customerId) {
        User customer = userRepository.findById(customerId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        int conversationId = conversationRepository.findConversationIdByCustomerId(customerId).orElse(-1);
        // Nếu chưa có conversation của customer -> (id = -1) thì tạo 1 conversation
        // Staff lấy id thằng ít conversation list nhất
        if (conversationId == -1) {
            Conversation c = conversationRepository.save(Conversation.builder()
                    .customer(customer)
                    .staff(userRepository.findById(userRepository.findLeastBusyStaffId().get())
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)))
                    .status(ConversationStatus.NEW)
                    .build());
        }

        return conversationRepository.findConversationByCustomerId(customerId);
    }

}
