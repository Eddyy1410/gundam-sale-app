package com.huyntd.superapp.gundam_shop.repository;

import com.huyntd.superapp.gundam_shop.dto.response.ConversationResponse;
import com.huyntd.superapp.gundam_shop.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    @Query("SELECT new com.huyntd.superapp.gundam_shop.dto.response.ConversationResponse(" +
            "c.id, " +
            "cust.fullName, " +
            "m.content, " +
            "m.sentAt, " +
            "cust.id " +
            ") " +
            "FROM Conversation c " +
            "JOIN c.customer cust " + // Giả sử có quan hệ @ManyToOne đến Customer
            "JOIN Message m ON m.conversation.id = c.id " +
            "WHERE c.staff.id = :staffId " + // Lọc theo admin hiện tại
            "AND m.sentAt = (SELECT MAX(m2.sentAt) FROM Message m2 WHERE m2.conversation.id = c.id) " +
            "ORDER BY m.sentAt DESC") // Sắp xếp để cuộc hội thoại có tin nhắn mới nhất lên đầu
    List<ConversationResponse> findConversationListForStaff(@Param("staffId") int staffId);
}
