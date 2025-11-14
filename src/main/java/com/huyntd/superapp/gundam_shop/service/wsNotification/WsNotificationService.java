package com.huyntd.superapp.gundam_shop.service.wsNotification;

import com.huyntd.superapp.gundam_shop.dto.enums.CountType;

public interface WsNotificationService {
    void sendUnreadCountUpdate(int receiverId, int newCount, CountType badgeType);
}
