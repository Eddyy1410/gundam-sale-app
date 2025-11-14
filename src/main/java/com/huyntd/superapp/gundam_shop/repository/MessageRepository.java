package com.huyntd.superapp.gundam_shop.repository;

import com.huyntd.superapp.gundam_shop.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId ORDER BY m.sentAt ASC")
    List<Message> findMessagesByConversationId(@Param("conversationId") int conversationId);

    @Query("SELECT COUNT(m) FROM Message m " +
            "JOIN m.conversation c " +
            "WHERE m.status = 'UNREAD' " +
            "AND m.sender.id != :receiverId " + // Người gửi KHÔNG PHẢI là người nhận
            "AND (c.customer.id = :receiverId OR c.staff.id = :receiverId)") // Tin nhắn thuộc conversation của họ
    int countUnreadMessagesForUser(@Param("receiverId") int receiverId);

    @Modifying // Đánh dấu đây là truy vấn sửa đổi (UPDATE/DELETE)
    @Transactional // Đảm bảo truy vấn được thực thi trong một Transaction
    @Query("UPDATE Message m SET m.status = 'READ' " +
            "WHERE m.status = 'UNREAD' " +
            "AND m.sender.id != :receiverId " + // Tin nhắn người khác gửi
            "AND m.conversation.id = :conversationId") // Tin nhắn thuộc Conversation cụ thể
    int markMessagesAsReadInConversation(@Param("receiverId") int receiverId,
                                         @Param("conversationId") int conversationId);
    // Nên trả về int vì nó sẽ trả về số lượng bản ghi đã bị ảnh hưởng (cập nhật hoặc xóa) bởi truy vấn.
}
