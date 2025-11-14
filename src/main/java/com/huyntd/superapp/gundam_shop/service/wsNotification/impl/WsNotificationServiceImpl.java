package com.huyntd.superapp.gundam_shop.service.wsNotification.impl;

import com.huyntd.superapp.gundam_shop.dto.enums.CountType;
import com.huyntd.superapp.gundam_shop.dto.wsResponse.CountResponse;
import com.huyntd.superapp.gundam_shop.exception.AppException;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import com.huyntd.superapp.gundam_shop.model.User;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;
import com.huyntd.superapp.gundam_shop.service.wsNotification.WsNotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class WsNotificationServiceImpl implements WsNotificationService {
    UserRepository userRepository;
    SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendUnreadCountUpdate(int receiverId, int newCount, CountType badgeType) {
        User receiver = userRepository.findById(receiverId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tạo một DTO đơn giản cho payload
        CountResponse payload = new CountResponse(newCount, badgeType);

        log.info("receiverId: {}, email: {}",receiverId,receiver.getEmail());
        log.info(payload.toString());
        // Dùng convertAndSendToUser để gửi đến topic cá nhân
        // Destination: /user/queue/unread-messages
        messagingTemplate.convertAndSendToUser(
                receiver.getEmail(),  // User ID đích
                "/queue/counting-messages",    // Topic cá nhân
                payload                      // Payload (DTO chứa số đếm)
        );
    }
}
