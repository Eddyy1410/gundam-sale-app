package com.huyntd.superapp.gundam_shop.service.order.impl;

import com.huyntd.superapp.gundam_shop.dto.request.CreateOrderRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UpdateOrderRequest;
import com.huyntd.superapp.gundam_shop.dto.response.OrderItemResponse;
import com.huyntd.superapp.gundam_shop.dto.response.OrderResponse;
import com.huyntd.superapp.gundam_shop.event.OrderCreatedEvent;
import com.huyntd.superapp.gundam_shop.exception.AppException;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import com.huyntd.superapp.gundam_shop.mapper.OrderMapper;
import com.huyntd.superapp.gundam_shop.model.Cart;
import com.huyntd.superapp.gundam_shop.model.Order;
import com.huyntd.superapp.gundam_shop.model.OrderItem;
import com.huyntd.superapp.gundam_shop.model.Product;
import com.huyntd.superapp.gundam_shop.model.enums.OrderStatus;
import com.huyntd.superapp.gundam_shop.model.enums.PaymentMethod;
import com.huyntd.superapp.gundam_shop.repository.OrderItemRepository;
import com.huyntd.superapp.gundam_shop.repository.OrderRepository;
import com.huyntd.superapp.gundam_shop.repository.ProductRepository;
import com.huyntd.superapp.gundam_shop.service.category.CategoryService;
import com.huyntd.superapp.gundam_shop.service.order.OrderService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class OrderServiceImpl implements OrderService {

    ProductRepository productRepository;
    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    ApplicationEventPublisher eventPublisher;
    OrderMapper orderMapper;

    @Override
    public Page<OrderResponse> getOrdersByStatus(Pageable pageable, String status,  int userId) {
        OrderStatus orderStatus;

        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }
        Page<Order> orderPage;

        if(userId == 0){
            orderPage = orderRepository.findAllByStatus(orderStatus, pageable)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
        } else {
            orderPage = orderRepository.findAllByStatusAndUserId(orderStatus, userId, pageable)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
        }

        // Dùng map() của Page để convert sang Page<OrderResponse>
        var page = orderPage.map(orderMapper::toOrderResponse);
        for(var item : page.getContent()) {
            var orderItemList = get(item.getId());
            item.setOrderItems(orderItemList.getOrderItems());
        }
        return page;
    }

    @Override
    public Page<OrderResponse> getCustomerOrders(Pageable pageable, int userId) {
        // Truy vấn phân trang (nên sửa repository để hỗ trợ Pageable)
        Page<Order> orderPage = orderRepository.findAllByUserId(userId, pageable)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Page<OrderResponse> order = orderPage.map(orderMapper::toOrderResponse);

        var page = orderPage.map(orderMapper::toOrderResponse);
        for(var item : page.getContent()) {
            var orderItemList = get(item.getId());
            item.setOrderItems(orderItemList.getOrderItems());
        }
        return page;
    }

    @Override
    public OrderResponse get(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(orderMapper::toOrderItemResponse)
                .toList();

        for(OrderItemResponse item : items){
            var product = productRepository.findById(item.getProductId());
            item.setProductName(product.get().getName());
            item.setProductImage(product.get().getProductImages().get(0).getImageUrl());
        }

        var orderResponse = orderMapper.toOrderResponse(order);
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

        List<Product> productsToUpdate = new ArrayList<>();
        List<OrderItem> orderItemsToSave = new ArrayList<>();

        for (var item : request.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + item.getProductId()));

            int remaining = product.getQuantity() - item.getQuantity();
            if (remaining < 0) {
                throw new RuntimeException("Insufficient stock for product id: " + item.getProductId());
            }

            // Chỉ cập nhật vào bộ nhớ tạm, chưa lưu
            product.setQuantity(remaining);
            productsToUpdate.add(product);

            var orderItem = orderMapper.toOrderItem(item);
            orderItem.setOrder(order);
            orderItemsToSave.add(orderItem);
        }

        productRepository.saveAll(productsToUpdate);
        orderItemRepository.saveAll(orderItemsToSave);

        eventPublisher.publishEvent(new OrderCreatedEvent(order));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse updateOrder(int id, UpdateOrderRequest request) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(request.getOrderStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        //Cập nhật số lượng
        var orderItemList = orderItemRepository.findAllByOrderId(order.getId());
        for (var item : orderItemList) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + item.getProduct().getId()));
            if (orderStatus.equals(OrderStatus.CANCELLED) || orderStatus.equals(OrderStatus.RETURNED)) {
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        }

        //Cập nhật Order
        orderMapper.updateOrderFromRequest(request, order);
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> getOrdersToday(int page, int size, String sortBy, String sortDir, String status) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy)
        );
        Page<Order> orderPage = orderRepository.findAllByStatus(OrderStatus.valueOf(status.toUpperCase()), pageable)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        return  orderPage.map(orderMapper::toOrderResponse);
    }

    @Override
    public OrderResponse updateOrderStatus(int id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status);
            order.setStatus(orderStatus);
            orderRepository.save(order);
            return orderMapper.toOrderResponse(order);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }
    }

    @Override
    public long getTodaysOrderCount() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return orderRepository.countOrdersToday(startOfDay, endOfDay);
    }

    @Override
    public long countPendingOrders() {
        return orderRepository.countByStatus(OrderStatus.PENDING);
    }
}
