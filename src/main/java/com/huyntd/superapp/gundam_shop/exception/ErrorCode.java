package com.huyntd.superapp.gundam_shop.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error!"),
    INVALID_KEY(1001, "Invalid enum error key!"),
    BLANK_EMAIL(1002, "Email must not be blank!"),
    BLANK_PASSWORD(1003, "Password must not be blank!"),
    USER_NOT_EXISTED(1004, "User not existed!"),
    USER_EXISTED(2000, "Email already existed!")

    ;

    int code;
    String message;

}
