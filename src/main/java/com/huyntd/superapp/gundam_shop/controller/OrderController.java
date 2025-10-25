package com.huyntd.superapp.gundam_shop.controller;

import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import com.huyntd.superapp.gundam_shop.dto.request.CreateOrderRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UpdateOrderRequest;
import com.huyntd.superapp.gundam_shop.dto.response.OrderResponse;
import com.huyntd.superapp.gundam_shop.service.order.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/order")
public class OrderController {

    OrderService orderService;

    @GetMapping("/status/user/{id}")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getOrdersStatus(@PathVariable int id, @RequestParam String status, Pageable pageable){
        var result = orderService.getOrdersByStatus(pageable, status, id);
        //userId = 0 -> Lấy all list
        return ResponseEntity.ok(
                ApiResponse.<Page<OrderResponse>>builder()
                        .result(result)
                        .build()
        );
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getCustomerOrders(@PathVariable int id, Pageable pageable) {
        var result = orderService.getCustomerOrders(pageable, id);

        return ResponseEntity.ok(
                ApiResponse.<Page<OrderResponse>>builder()
                        .result(result)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends Object> getOrderDetail(@PathVariable int id) {
        var result = orderService.get(id);

        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .result(result)
                        .build()
        );
    }

    @PostMapping()
    public ResponseEntity<? extends Object> create (@RequestBody CreateOrderRequest request) {
        var order = orderService.createOrder(request);
        return ResponseEntity.ok(ApiResponse.<OrderResponse>builder()
                .result(order)
                .build());
    }

    @PutMapping("{id}")
    public ResponseEntity<? extends Object> update (@PathVariable int id, @RequestBody UpdateOrderRequest request) {
        var order = orderService.updateOrder(id, request);
        return ResponseEntity.ok(ApiResponse.<OrderResponse>builder()
                .result(order)
                .build());
    }
}
