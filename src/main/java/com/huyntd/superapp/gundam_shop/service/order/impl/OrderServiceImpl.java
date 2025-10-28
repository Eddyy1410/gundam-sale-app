package com.huyntd.superapp.gundam_shop.service.order.impl;

import com.huyntd.superapp.gundam_shop.dto.request.CreateOrderRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UpdateOrderRequest;
import com.huyntd.superapp.gundam_shop.dto.response.OrderItemResponse;
import com.huyntd.superapp.gundam_shop.dto.response.OrderResponse;
import com.huyntd.superapp.gundam_shop.exception.AppException;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import com.huyntd.superapp.gundam_shop.mapper.OrderMapper;
import com.huyntd.superapp.gundam_shop.model.Cart;
import com.huyntd.superapp.gundam_shop.model.Order;
import com.huyntd.superapp.gundam_shop.model.OrderItem;
import com.huyntd.superapp.gundam_shop.model.enums.OrderStatus;
import com.huyntd.superapp.gundam_shop.model.enums.PaymentMethod;
import com.huyntd.superapp.gundam_shop.repository.OrderItemRepository;
import com.huyntd.superapp.gundam_shop.repository.OrderRepository;
import com.huyntd.superapp.gundam_shop.service.category.CategoryService;
import com.huyntd.superapp.gundam_shop.service.order.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;

    OrderMapper orderMapper;

    @Override
    public Page<OrderResponse> getOrdersByStatus(Pageable pageable, String status) {
        OrderStatus orderStatus;

        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        Page<Order> orderPage = orderRepository.findAllByStatus(orderStatus, pageable)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Dùng map() của Page để convert sang Page<OrderResponse>
        return orderPage.map(orderMapper::toOrderResponse);
    }

    @Override
    public Page<OrderResponse> getCustomerOrders(Pageable pageable, int userId) {
        // Truy vấn phân trang (nên sửa repository để hỗ trợ Pageable)
        Page<Order> orderPage = orderRepository.findAllByUserId(userId, pageable)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Page<OrderResponse> order =  orderPage.map(orderMapper::toOrderResponse);

        return orderPage.map(orderMapper::toOrderResponse);
    }

    @Override
    public OrderResponse get(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(orderMapper::toOrderItemResponse)
                .toList();

        var orderResponse =  orderMapper.toOrderResponse(order);
        orderResponse.setOrderItems(items);

        return orderResponse;
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        var order = orderMapper.toOrder(request);
        order.setOrderDate(LocalDateTime.now());

        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(paymentMethod);
        var orderResponse = orderRepository.save(order);

        for(var item: request.getOrderItems()) {
            var orderItem = orderMapper.toOrderItem(item);
            orderItem.setOrder(order);

            orderItemRepository.save(orderItem);
        }

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse updateOrder(int id, UpdateOrderRequest request) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        orderMapper.updateOrderFromRequest(request, order);
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> getOrdersToday(int page, int size, String sortBy, String sortDir, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<Order> orderPage = orderRepository.findAllByStatus(OrderStatus.valueOf(status.toUpperCase()), pageable)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        return  orderPage.map(orderMapper::toOrderResponse);
    }
}
