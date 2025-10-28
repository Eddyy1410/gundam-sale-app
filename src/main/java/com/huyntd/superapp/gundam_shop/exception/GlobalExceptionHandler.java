package com.huyntd.superapp.gundam_shop.exception;

import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
//@ControllerAdvice là một annotation cho phép bạn tạo ra các class xử lý logic chung mà bạn muốn áp dụng cho toàn bộ hoặc một nhóm các Controller.
//Nó giúp bạn tách biệt các "mối quan tâm xuyên suốt" (cross-cutting concerns) ra khỏi logic nghiệp vụ chính của Controller.
// 1. @ExceptionHandler: Xử lý ngoại lệ tập trung
// 2. @ModelAttribute: Thêm dữ liệu chung vào Model
// 3. @InitBinder: Tùy chỉnh việc liên kết dữ liệu (Data Binding)
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<ApiResponse> handlingException(Exception ex) {
//        System.out.println(ex);
//        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode()).body(
//                ApiResponse.builder()
//                        .success(false)
//                        .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
//                        .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
//                        .build()
//        );
//    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException ex) {
        return ResponseEntity.status(ex.getErrorCode().getStatusCode()).body(
                ApiResponse.builder()
                        .success(false)
                        .code(ex.getErrorCode().getCode())
                        .message(ex.getErrorCode().getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getStatusCode()).body(
                ApiResponse.builder()
                        .success(false)
                        .code(ErrorCode.UNAUTHORIZED.getCode())
                        .message(ErrorCode.UNAUTHORIZED.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(ex.getFieldError().getDefaultMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.badRequest().body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .success(false)
                .build());
    }

}
