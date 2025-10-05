package com.huyntd.superapp.gundam_shop.model.enums;

public enum OrderStatus {
    PENDING,        // Chờ xử lý
    PROCESSING,     // Đang xử lý
    SHIPPED,        // Đã giao vận chuyển
    DELIVERED,      // Đã giao thành công
    CANCELLED,      // Đã hủy
    RETURNED,       // Bị trả lại
    REFUNDED        // Đã hoàn tiền
}
