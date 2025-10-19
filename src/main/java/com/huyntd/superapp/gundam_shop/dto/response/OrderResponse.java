package com.huyntd.superapp.gundam_shop.dto.response;

import com.huyntd.superapp.gundam_shop.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    int id;
    String billingAddress;
    LocalDateTime orderDate;
    String status;
    String paymentMethod;
    double totalPrice;
    List<OrderItemResponse> orderItems;
}
