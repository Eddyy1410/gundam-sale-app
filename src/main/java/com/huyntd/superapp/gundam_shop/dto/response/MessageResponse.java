package com.huyntd.superapp.gundam_shop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
    int id;
    int senderId; // ID của user gửi (đã xác thực)
    String senderName;
    String content;
    Date sentAt;
}
