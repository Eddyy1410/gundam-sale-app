package com.huyntd.superapp.gundam_shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error!", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid enum error key!", HttpStatus.BAD_REQUEST),
    BLANK_EMAIL(1002, "Email must not be blank!", HttpStatus.BAD_REQUEST),
    BLANK_NAME(1012, "Name must not be blank!", HttpStatus.BAD_REQUEST),
    BLANK_PASSWORD(1003, "Password must not be blank!", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1008, "Email is not valid!", HttpStatus.BAD_REQUEST),
    WEAK_PASSWORD(1009, "Password is too weak! Must be at least 8 characters and include letters and numbers.", HttpStatus.BAD_REQUEST),
    MAIL_SEND_FAILED(1010, "Failed to send email for password reset.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_CODE(1011, "Reset code is invalid.", HttpStatus.BAD_REQUEST),
    INVALID_PHONE(1013, "Phone number is not valid.", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004, "User not existed!", HttpStatus.NOT_FOUND),
    USER_EXISTED(1005, "Email already existed!", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission to access this resource!", HttpStatus.FORBIDDEN),
    CONVERSATION_ACCESS_DENIED(1008, "You are not authorized to send messages in this conversation!", HttpStatus.FORBIDDEN),
    CUSTOMER_NOT_EXISTED(1009, "Customer not existed!", HttpStatus.NOT_FOUND),
    ;

    int code;
    String message;
    HttpStatusCode statusCode;

}
