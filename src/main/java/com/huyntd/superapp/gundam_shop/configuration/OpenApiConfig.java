package com.huyntd.superapp.gundam_shop.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// JpaAuditing để cho cái CreatedDate với UpdatedDate tự động cập nhật, để đại ở đây
@EnableJpaAuditing
@Configuration
// 1. Định nghĩa Security Scheme
@SecurityScheme(
        name = "bearerAuth", // Tên của security scheme, sẽ được dùng lại ở dưới
        type = SecuritySchemeType.HTTP, // Loại scheme là HTTP
        scheme = "bearer", // Scheme cụ thể là "bearer"
        bearerFormat = "JWT", // Định dạng của token
        in = SecuritySchemeIn.HEADER, // Vị trí của token trong request
        description = "Enter JWT Bearer token" // Mô tả hiển thị trên UI
)
// 2. Định nghĩa thông tin chung và áp dụng Security Scheme toàn cục
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Gundam Shop API",
                version = "1.0.0",
                description = "RESTful APIs for Gundam Shop Application"
        ),
        // Áp dụng security scheme có tên "bearerAuth" cho TẤT CẢ các endpoint
        security = @SecurityRequirement(name = "bearerAuth")
)
public class OpenApiConfig {

}
