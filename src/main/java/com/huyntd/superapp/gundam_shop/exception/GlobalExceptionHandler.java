package com.huyntd.superapp.gundam_shop.exception;

import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingException(Exception ex) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        apiResponse.setSuccess(false);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> AppException(AppException ex) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(ex.getErrorCode().getCode());
        apiResponse.setMessage(ex.getErrorCode().getMessage());
        apiResponse.setSuccess(false);
        return ResponseEntity.badRequest().body(apiResponse);
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
