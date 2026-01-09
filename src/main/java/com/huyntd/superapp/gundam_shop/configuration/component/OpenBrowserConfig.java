package com.huyntd.superapp.gundam_shop.configuration.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OpenBrowserConfig {

    // Lấy cổng server từ file properties (mặc định 8080 nếu không có)
    @Value("${server.port:8080}")
    private String port;

    // Lấy context-path nếu bạn có cấu hình (ví dụ /api)
    @Value("${server.servlet.context-path:}")
    private String contextPath;

    // Lấy đường dẫn swagger đã cấu hình ở trên
    @Value("${springdoc.swagger-ui.path:/swagger-ui.html}")
    private String swaggerPath;

    @EventListener(ApplicationReadyEvent.class)
    public void openSwagger() {
        String url = "http://localhost:" + port + contextPath + swaggerPath;

        System.out.println("--- Đang tự động mở Swagger UI tại: " + url + " ---");

        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();

        try {
            if (os.contains("win")) {
                // Windows
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("mac")) {
                // MacOS
                rt.exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux
                rt.exec("xdg-open " + url);
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi tự động mở trình duyệt: " + e.getMessage());
        }
    }
}