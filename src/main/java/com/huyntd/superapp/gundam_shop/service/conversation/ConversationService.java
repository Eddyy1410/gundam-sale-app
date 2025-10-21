package com.huyntd.superapp.gundam_shop.service.conversation;

import com.huyntd.superapp.gundam_shop.dto.response.ConversationResponse;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationService {
    List<ConversationResponse> findConversationListForStaff(@Param("staffId") int staffId);
}
