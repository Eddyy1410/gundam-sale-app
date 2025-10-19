package com.huyntd.superapp.gundam_shop.mapper;

import com.huyntd.superapp.gundam_shop.dto.request.CreateOrderRequest;
import com.huyntd.superapp.gundam_shop.dto.request.OrderItemRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UpdateOrderRequest;
import com.huyntd.superapp.gundam_shop.dto.response.OrderItemResponse;
import com.huyntd.superapp.gundam_shop.dto.response.OrderResponse;
import com.huyntd.superapp.gundam_shop.model.Order;
import com.huyntd.superapp.gundam_shop.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "orderItems", ignore = true)
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "product.id", source = "productId")
    OrderItem toOrderItem(OrderItemRequest orderItemRequest);

    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "user.id", source = "userId")
    Order toOrder(CreateOrderRequest orderRequest);

    // Dành cho update (chỉ map field có trong request)
    void updateOrderFromRequest(UpdateOrderRequest request, @MappingTarget Order order);
}
