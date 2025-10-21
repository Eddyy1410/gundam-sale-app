package com.huyntd.superapp.gundam_shop.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error!", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid enum error key!", HttpStatus.BAD_REQUEST),
    BLANK_EMAIL(1002, "Email must not be blank!", HttpStatus.BAD_REQUEST),
    BLANK_PASSWORD(1003, "Password must not be blank!", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004, "User not existed!", HttpStatus.NOT_FOUND),
    USER_EXISTED(1005, "Email already existed!", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission to access this resource!", HttpStatus.FORBIDDEN)
    ;

    int code;
    String message;
    HttpStatusCode statusCode;

}
