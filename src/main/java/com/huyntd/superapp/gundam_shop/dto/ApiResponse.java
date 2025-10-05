package com.huyntd.superapp.gundam_shop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Getter, Setter, RequiredConstructor, ToString, EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T> {

    // Không có @Builder.Default là lúc build sẽ tự gán giá trị mặc định của kiểu dữ liệu theo Java
    @Builder.Default
    boolean success = true;

    @Builder.Default
    int code = 1000; // Default code = 1000 là success
    // **Phân biệt: code ApiResponse khác với Http Status Code của ResponseEntity**
    // HTTP Code 400 cho biết loại lỗi chung là "lỗi từ client".
    // JSON Body với code: 1001 và message cho biết lý do cụ thể là "email đã tồn tại".
    // HTTP status code là chuẩn cho ngôn ngữ kỹ thuật chung, là phân loại
    // Code của ApiResponse là cho nội bộ ứng dụng, ngôn ngữ nghiệp vụ và cần được phân loại vào loại HTTP status code thích hợp

    String message;

    T result;

}
