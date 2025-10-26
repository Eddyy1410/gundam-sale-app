package com.huyntd.superapp.gundam_shop.controller;

import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import com.huyntd.superapp.gundam_shop.service.firebase.FcmService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    FcmService fcmService;

    @PostMapping("/send")
    public ApiResponse<?> sendNotification() {
        String targetToken = "TARGET_DEVICE_FCM_TOKEN"; // Replace with actual target token
        String title = "Test Notification";
        String body = "This is a test notification from FCM.";

        String response = fcmService.sendNotification(targetToken, title, body);

        return ApiResponse.builder()
                .result(response)
                .message("Notification sent successfully")
                .build();
    }

}
