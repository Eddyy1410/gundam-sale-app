package com.huyntd.superapp.gundam_shop.dto;

import com.huyntd.superapp.gundam_shop.model.enums.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPrincipal implements Serializable {

    // CHỈ LẤY CÁC TRƯỜNG NGUYÊN THỦY (Primitive) HOẶC SERIALIZABLE
    int id;
    String email;
    String fullName;
    UserRole role; // Enum là Serializable
    // Không thêm các List<Conversation>, Cart, Order...

}
