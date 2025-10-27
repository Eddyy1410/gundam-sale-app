package com.huyntd.superapp.gundam_shop.configuration.event;


import com.huyntd.superapp.gundam_shop.event.OrderCreatedEvent;
import com.huyntd.superapp.gundam_shop.model.Notification;
import com.huyntd.superapp.gundam_shop.model.User;
import com.huyntd.superapp.gundam_shop.repository.NotificationRepository;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;

import com.huyntd.superapp.gundam_shop.service.firebase.FcmService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;

    // 🔁 Chạy sau khi transaction commit thành công
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event) {
        var order = event.getOrder();

        List<User> staffList = userRepository.findByRole("STAFF");

        staffList.forEach(staff -> {
            // 1️⃣ Tạo bản ghi notification (tùy chọn)
            Notification notification = Notification.builder()
                    .user(staff)
                    .message("Đơn hàng #" + order.getId() + " vừa được tạo bởi user #" + order.getUser().getId())
                    .isRead(false)
                    .build();
            notificationRepository.save(notification);

            // 2️⃣ Gửi FCM nếu có token
            if (staff.getFcmToken() != null && !staff.getFcmToken().isBlank()) {
                String title = "Đơn hàng mới #" + order.getId();
                String body = "Khách hàng #" + order.getUser().getId() + " vừa tạo đơn hàng mới.";
                fcmService.sendNotification(staff.getFcmToken(), title, body);
            }
        });
    }
}
