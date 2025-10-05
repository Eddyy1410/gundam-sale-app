package com.huyntd.superapp.gundam_shop.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppException extends RuntimeException {

    ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // Phải xây dựng hoàn chỉnh lớp cha, sau đó mới thêm các thuộc tính lớp con
        this.errorCode = errorCode;

        // ở đây errorCode.getMessage sẽ được lưu ở 2 nơi
        // Tuy nhiên là best practise không phải là dư thừa dữ liệu
        // 1. super(message): Để AppException tương thích với các công cụ Java tiêu chuẩn, phục vụ cho việc logging và debugging.
        // 2. this.errorCode: Để AppException cung cấp ngữ cảnh lỗi đầy đủ, phục vụ cho việc xử lý lỗi có cấu trúc trong ứng dụng của bạn (ví dụ: trong @ControllerAdvice).
    }
}
