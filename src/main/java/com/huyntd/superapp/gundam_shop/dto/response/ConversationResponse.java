package com.huyntd.superapp.gundam_shop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationResponse {
    private int conversationId;
    private String customerName;
    private String latestMessageContent;
    private Date latestMessageSentAt;
    private int customerId;
    // 2 trường dưới này CỰC KỲ HỮU ÍCH
    //private String customerAvatarUrl; // URL ảnh đại diện của khách hàng
    //private int unreadMessageCount; // Số tin nhắn chưa đọc
}
