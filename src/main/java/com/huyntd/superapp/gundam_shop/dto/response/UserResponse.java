package com.huyntd.superapp.gundam_shop.dto.response;

import com.huyntd.superapp.gundam_shop.model.enums.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;


@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    int id;
    String email;
    String fullName;
    String phone;
    UserRole role;
    Date createdAt;
}
