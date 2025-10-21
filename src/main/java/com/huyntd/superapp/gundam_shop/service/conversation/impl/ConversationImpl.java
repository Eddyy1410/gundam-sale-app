package com.huyntd.superapp.gundam_shop.service.conversation.impl;

import com.huyntd.superapp.gundam_shop.dto.response.ConversationResponse;
import com.huyntd.superapp.gundam_shop.repository.ConversationRepository;
import com.huyntd.superapp.gundam_shop.service.conversation.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ConversationImpl implements ConversationService {

    ConversationRepository conversationRepository;

    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    @Override
    public List<ConversationResponse> findConversationListForStaff(int staffId) {
        return conversationRepository.findConversationListForStaff(staffId);
    }
}
