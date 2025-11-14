package com.huyntd.superapp.gundam_shop.dto.request;

import com.huyntd.superapp.gundam_shop.dto.response.OrderItemResponse;
import com.huyntd.superapp.gundam_shop.model.OrderItem;
import com.huyntd.superapp.gundam_shop.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private int userId;
    private double totalPrice;
    private String paymentMethod;
    private String billingAddress;
    private List<OrderItemRequest> orderItems;
}
