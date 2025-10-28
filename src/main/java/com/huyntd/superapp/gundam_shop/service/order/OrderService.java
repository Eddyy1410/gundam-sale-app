package com.huyntd.superapp.gundam_shop.service.order;

import com.huyntd.superapp.gundam_shop.dto.request.CreateOrderRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UpdateOrderRequest;
import com.huyntd.superapp.gundam_shop.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderResponse> getOrdersByStatus (Pageable pageable, String status, int userId);
    Page<OrderResponse> getCustomerOrders (Pageable pageable, int userId);
    OrderResponse get (int orderId);
    OrderResponse createOrder (CreateOrderRequest request);
    OrderResponse updateOrder (int id, UpdateOrderRequest request);
}
