package com.huyntd.superapp.gundam_shop.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class WebSocketErrorHandler {

    // Kênh lỗi: /user/queue/errors
    public static final String ERROR_QUEUE = "/queue/errors";

    // Xử lý các ngoại lệ nghiệp vụ của bạn
    @MessageExceptionHandler(AppException.class)
    @SendToUser(ERROR_QUEUE)
    public WebSocketErrorResponse handleAppException(AppException ex) {
        log.error("WebSocket AppException: {}", ex.getErrorCode().getMessage());
        return new WebSocketErrorResponse(ex.getErrorCode());
    }

    // Xử lý lỗi không xác định (Internal Server Error)
    @MessageExceptionHandler(Exception.class)
    @SendToUser(ERROR_QUEUE)
    public WebSocketErrorResponse handleGenericException(Exception ex) {
        log.error("UNCAUGHT WebSocket ERROR: ", ex);
        return new WebSocketErrorResponse(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }

}
